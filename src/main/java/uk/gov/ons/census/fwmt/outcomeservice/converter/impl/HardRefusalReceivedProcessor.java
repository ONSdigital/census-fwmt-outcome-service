package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.census.ffa.storage.utils.StorageUtils;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomeSetup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.RefusalEncryptionLookup;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.census.fwmt.outcomeservice.util.EncryptNames;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceLogConfig.*;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;

@Transactional
@Component("HARD_REFUSAL_RECEIVED")
public class HardRefusalReceivedProcessor implements OutcomeServiceProcessor {

  @Autowired
  private DateFormat dateFormat;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomeSetup outcomeSetup;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Autowired
  private RefusalEncryptionLookup refusalEncryptionLookup;

  @Autowired
  public byte[] fwmtPgpPublicKeyByteArray;

  @Autowired
  public byte[] midlPgpPublicKeyByteArray; 
  
  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    boolean isHouseHolder = false;
    String encryptedTitle = "";
    String encryptedForename = "";
    String encryptedSurname = "";
    String foreName;
    String middleName;
    String combinedNames = "";
    boolean correctType;

    String refusalCodes = refusalEncryptionLookup.getLookup(outcome.getOutcomeCode());

    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));

    cacheData(outcome, caseId, type, cache);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
        SURVEY_TYPE, type,
        PROCESSOR, "HARD_REFUSAL_RECEIVED",
        ORIGINAL_CASE_ID, String.valueOf(outcome.getCaseId()),
        SITE_CASE_ID, (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));
    
    correctType = type.equals("HH") || type.equals("NC");

    if (refusalCodes != null && correctType && outcome.getRefusal() != null) {
      isHouseHolder = outcome.getRefusal().isHouseholder();
      encryptedTitle = outcome.getRefusal().getTitle() != null && !outcome.getRefusal().getTitle().isBlank() ?
          returnEncryptedNames(outcome.getRefusal().getTitle()) : "";

      foreName = outcome.getRefusal().getFirstname() != null && !outcome.getRefusal().getFirstname().isBlank() ?
          outcome.getRefusal().getFirstname() : "";

      middleName = outcome.getRefusal().getMiddlenames() != null && !outcome.getRefusal().getMiddlenames().isBlank() ?
          outcome.getRefusal().getMiddlenames() : "";

      if (!foreName.isBlank() && !middleName.isBlank()) {
        combinedNames = foreName + " " + middleName;
      } else if (!foreName.isBlank()) {
        combinedNames = foreName;
      } else if (!middleName.isBlank()) {
        combinedNames = middleName;
      }

      encryptedForename = !combinedNames.isBlank() ? returnEncryptedNames(combinedNames) : "";

      encryptedSurname = outcome.getRefusal().getSurname() != null && !outcome.getRefusal().getSurname().isBlank() ?
          returnEncryptedNames(outcome.getRefusal().getSurname()) : "";
    }

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("outcome", outcome);
    root.put("type", type);
    root.put("refusalType", "HARD_REFUSAL");
    root.put("officerId", outcome.getOfficerId());
    root.put("caseId", caseId);
    root.put("eventDate", eventDateTime);
    root.put("isHouseHolder", isHouseHolder);
    root.put("encryptedTitle", encryptedTitle);
    root.put("encryptedForename", encryptedForename);
    root.put("encryptedSurname", encryptedSurname);
    root.put("refusalCodes", refusalCodes);

    try {
      TimeUnit.MILLISECONDS.sleep(outcomeSetup.getMessageProcessorSleepTime());
    } catch (InterruptedException ignored) {}

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root);
    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
            GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), OUTCOME_SENT,
        SURVEY_TYPE, type,
        TEMPLATE_TYPE, REFUSAL_RECEIVED.toString(),
        TRANSACTION_ID, outcome.getTransactionId().toString(),
        ROUTING_KEY, GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);

    return caseId;
  }

  protected String returnEncryptedNames(String names) throws GatewayException {
    String formatNames;
    var publicKeys = new ArrayList<InputStream>();
    publicKeys.add(new ByteArrayInputStream(fwmtPgpPublicKeyByteArray));
    publicKeys.add(new ByteArrayInputStream(midlPgpPublicKeyByteArray));
    formatNames = EncryptNames.receivedNames(names, publicKeys);

    return Base64.getEncoder().encodeToString(formatNames.getBytes(Charset.defaultCharset()));
  }

  private void cacheData(OutcomeSuperSetDto outcome, UUID caseId, String type, GatewayCache cache)
      throws GatewayException {
    int typeCache = type.equals("CE") ? 1 : 10;
    String dangerousCareCode;
    String updateCareCodes;
    if (type.equals("HH")) {
      dangerousCareCode = outcome.getRefusal().isDangerous()  ? "Dangerous address" : "No safety issues";
      updateCareCodes = outcome.getCareCodes() != null ? OutcomeSuperSetDto.careCodesToText(outcome.getCareCodes()) + ", " + dangerousCareCode :
          dangerousCareCode;
    } else {
      updateCareCodes = OutcomeSuperSetDto.careCodesToText(outcome.getCareCodes());
    }

    if (cache == null) {
      gatewayEventManager.triggerErrorEvent(HardRefusalReceivedProcessor.class,
          "Cache lookup for hard refusal has not returned a case within cache.",
          caseId.toString(), "Case never received by Job Service",
          SURVEY_TYPE, type,
          OUTCOME, outcome.toString());
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case did not exist in cache");
    } else {
      gatewayCacheService.save(cache.toBuilder()
          .accessInfo(outcome.getAccessInfo())
          .careCodes(updateCareCodes)
          .type(typeCache)
          .build());
    }
  }
}
