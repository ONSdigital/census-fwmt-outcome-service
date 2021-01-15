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
import java.net.URI;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;

@Transactional
@Component("HARD_REFUSAL_RECEIVED")
public class HardRefusalReceivedProcessor implements OutcomeServiceProcessor {

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String OUTCOME_SENT = "OUTCOME_SENT";

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
  private StorageUtils storageUtils;

  @Value("${outcomeservice.pgp.fwmtPublicKey}")
  private String fwmtPgpPublicKey;

  @Value("${outcomeservice.pgp.midlPublicKey}")
  private String midlPgpPublicKey;

  @Value("${outcomeservice.pgp.fwmtPublicKey}")
  private String directory;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    boolean isHouseHolder = false;
    String encryptedTitle = "";
    String encryptedForename = "";
    String encryptedSurname = "";

    String refusalCodes = refusalEncryptionLookup.getLookup(outcome.getOutcomeCode());

    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));

    cacheData(outcome,caseId, type, cache);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
    "survey type", type,
    "processor", "HARD_REFUSAL_RECEIVED",
    "original caseId", String.valueOf(outcome.getCaseId()),
    "Site Case id", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));
    if (refusalCodes != null) {
      if (outcome.getRefusal() != null && (type.equals("HH") || type.equals("NC"))) {
        if (outcome.getRefusal().isHouseholder() && (outcome.getRefusal().getSurname() != null
            && !outcome.getRefusal().getSurname().equals(""))) {
          isHouseHolder = outcome.getRefusal().isHouseholder();
          encryptedTitle = outcome.getRefusal().getTitle() != null ?
              returnEncryptedNames(outcome.getRefusal().getTitle()) : "";
          if (outcome.getRefusal().getMiddlenames() != null && !outcome.getRefusal().getMiddlenames().equals("")) {
            String combinedNames = outcome.getRefusal().getFirstname() + " " + outcome.getRefusal().getMiddlenames();
            encryptedForename = returnEncryptedNames(combinedNames);
          } else {
            encryptedForename = outcome.getRefusal().getFirstname() != null ?
                returnEncryptedNames(outcome.getRefusal().getFirstname()) :"";
          }
          encryptedSurname = returnEncryptedNames(outcome.getRefusal().getSurname());
        }
      }
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
        "survey type", type,
        "type", REFUSAL_RECEIVED.toString(),
        "transactionId", outcome.getTransactionId().toString(),
        "routing key", GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);

    return caseId;
  }

  protected String returnEncryptedNames(String names) throws GatewayException {
    String formatNames;
    var publicKeys = new ArrayList<InputStream>();
    publicKeys.add(storageUtils.getFileInputStream(URI.create(fwmtPgpPublicKey)));
    publicKeys.add(storageUtils.getFileInputStream(URI.create(midlPgpPublicKey)));
    formatNames = EncryptNames.receivedNames(names, publicKeys);

    return Base64.getEncoder().encodeToString(formatNames.getBytes(Charset.defaultCharset()));
  }

  private void cacheData(OutcomeSuperSetDto outcome, UUID caseId, String type, GatewayCache cache)
      throws GatewayException {
    int typeCache = type.equals("CE") ? 1 : 10;
    String dangerousCareCode = "";
    String updateCareCodes = "";
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
          "type", type,
          "outcome", outcome.toString());
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
