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

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.NEW_ADDRESS_REPORTED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.NEW_ADDRESS_REPORTED;
import static uk.gov.ons.census.fwmt.outcomeservice.util.SpgUtilityMethods.isDelivered;
import static uk.gov.ons.census.fwmt.outcomeservice.util.SpgUtilityMethods.regionLookup;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_OUTCOME;

@Component("NEW_ADDRESS_REPORTED")
public class NewAddressReportedProcessor implements OutcomeServiceProcessor {

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
    "processor", "NEW_ADDRESS_REPORTED",
    "original caseId", String.valueOf(outcome.getCaseId()));

    boolean isDelivered = isDelivered(outcome);
    cacheData(outcome, caseId, isDelivered);

    String eventDateTime = dateFormat.format(outcome.getEventDate());

    Map<String, Object> root = new HashMap<>();
    root.put("sourceCase", "NEW_STANDALONE");
    root.put("outcome", outcome);
    root.put("newCaseId", caseId);
    root.put("region", regionLookup(outcome.getOfficerId()));
    root.put("address", outcome.getAddress());
    root.put("officerId", outcome.getOfficerId());
    root.put("eventDate", eventDateTime);
    root.put("addressType", type);


    String outcomeEvent = TemplateCreator.createOutcomeMessage(NEW_ADDRESS_REPORTED, root);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(caseId), OUTCOME_SENT,
        "survey type", type,
        "type", NEW_ADDRESS_REPORTED.toString(),
        "transaction id", outcome.getTransactionId().toString(),
        "routing key", GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);

    return caseId;
  }

  private void cacheData(OutcomeSuperSetDto outcome, UUID caseId, boolean isDelivered) throws GatewayException {
    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));
    if (cache != null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case already exists in cache: {}", caseId);
    }

    gatewayCacheService.save(GatewayCache.builder()
        .caseId(String.valueOf(caseId))
        .delivered(isDelivered)
        .existsInFwmt(false)
        .accessInfo(outcome.getAccessInfo())
        .careCodes(OutcomeSuperSetDto.careCodesToText(outcome.getCareCodes()))
        .type(0)
        .build());
  }
}
