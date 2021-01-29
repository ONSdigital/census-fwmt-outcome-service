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

import static uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceLogConfig.*;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.CCS_ADDRESS_LISTED;

@Component("PROPERTY_LISTED_CE")
public class PropertyListedCeProcessor implements OutcomeServiceProcessor {

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
    UUID newCaseId = UUID.randomUUID();

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
        SURVEY_TYPE, type,
        PROCESSOR, "PROPERTY_LISTED_CE",
        ORIGINAL_CASE_ID, String.valueOf(outcome.getCaseId()),
        PROPERTY_LISTED_CASE_ID, String.valueOf(newCaseId),
        ADDRESS_TYPE, "CE");

    GatewayCache plCache = gatewayCacheService.getById(String.valueOf(caseId));

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("outcome", outcome);
    root.put("address", outcome.getAddress());
    root.put("caseId", newCaseId);
    root.put("eventDate", eventDateTime);
    root.put("addressType", "CE");
    root.put("addressLevel", "E");
    root.put("interviewRequired", "False");
    root.put("oa", plCache.getOa());
    root.put("region",plCache.getOa().charAt(0));
    root.put("estabType", outcome.getCeDetails() != null && outcome.getCeDetails().getEstablishmentType() != null ?
        outcome.getCeDetails().getEstablishmentType() : "CE");
    root.put("organisationName", outcome.getCeDetails() != null && outcome.getCeDetails().getEstablishmentName() != null ?
        outcome.getCeDetails().getEstablishmentName() : "");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(CCS_ADDRESS_LISTED, root);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_CCS_PROPERTY_LISTING_ROUTING_KEY);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), OUTCOME_SENT,
        SURVEY_TYPE, type,
        TEMPLATE_TYPE, CCS_ADDRESS_LISTED.toString(),
        TRANSACTION_ID, outcome.getTransactionId().toString(),
        ROUTING_KEY, GatewayOutcomeQueueConfig.GATEWAY_CCS_PROPERTY_LISTING_ROUTING_KEY);

    return newCaseId;
  }
}
