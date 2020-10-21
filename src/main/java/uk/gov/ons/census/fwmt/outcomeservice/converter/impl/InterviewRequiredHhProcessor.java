package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.INTERVIEW_REQUIRED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.CCS;

@Component("INTERVIEW_REQUIRED_HH")
public class InterviewRequiredHhProcessor implements OutcomeServiceProcessor {

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String OUTCOME_SENT = "OUTCOME_SENT";

  @Autowired
  private DateFormat dateFormat;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
        "survey type", type,
        "processor", "INTERVIEW_REQUIRED",
        "original caseId", String.valueOf(outcome.getCaseId()),
        "Site case Id", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"),
        "addressType", "HH");

    GatewayCache cache = gatewayCacheService.getById(String.valueOf(outcome.getSiteCaseId()));

    cacheData(outcome, outcome.getSiteCaseId(), caseId);

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("outcome", outcome);
    root.put("address", outcome.getAddress());
    root.put("caseId", caseId);
    root.put("eventDate", eventDateTime);
    root.put("addressType", "HH");
    root.put("addressLevel", "U");
    root.put("interviewRequired", "True");
    root.put("oa", cache.getOa());
    root.put("region",cache.getOa().substring(0,2));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(CCS, root);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(caseId), OUTCOME_SENT,
        "survey type", type,
        "type", INTERVIEW_REQUIRED.toString(),
        "transactionId", outcome.getTransactionId().toString(),
        "routing key", GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);

    return caseId;
  }

  private void cacheData(OutcomeSuperSetDto outcome, UUID plCaseId, UUID newCaseId) throws GatewayException {
    GatewayCache parentCacheJob = gatewayCacheService.getById(plCaseId.toString());
    if (parentCacheJob == null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Parent case does not exist in cache: {}", plCaseId);
    }

    GatewayCache newCachedJob = gatewayCacheService.getById(newCaseId.toString());
    if (newCachedJob != null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "New case exists in cache: {}", plCaseId);
    }

   
    gatewayCacheService.save(GatewayCache.builder()
        .caseId(newCaseId.toString())
        .existsInFwmt(false)
        .accessInfo(outcome.getAccessInfo())
        .careCodes(OutcomeSuperSetDto.careCodesToText(outcome.getCareCodes()))
        .type(50)
        .managerTitle(outcome.getCeDetails().getManagerTitle())
        .managerFirstname(outcome.getCeDetails().getManagerForename())
        .managerSurname(outcome.getCeDetails().getManagerSurname())
        .build());
  }


}
