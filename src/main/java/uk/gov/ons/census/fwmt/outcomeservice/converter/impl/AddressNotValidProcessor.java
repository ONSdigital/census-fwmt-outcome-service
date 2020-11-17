package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.ReasonCodeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;

@Component("ADDRESS_NOT_VALID")
public class AddressNotValidProcessor implements OutcomeServiceProcessor {

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String OUTCOME_SENT = "OUTCOME_SENT";

  public static final String FAILED_TO_LOOKUP_REASON_CODE = "FAILED_TO_LOOKUP_REASON_CODE";

  @Autowired
  private DateFormat dateFormat;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private ReasonCodeLookup reasonCodeLookup;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();
    
    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
    "Survey type", type,
    "Processor", "ADDRESS_NOT_VALID",
    "Case ID", String.valueOf(outcome.getCaseId()),
    "Site Case ID", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));

    String reasonCode = reasonCodeLookup.getLookup(outcome.getOutcomeCode());

    if (reasonCode.equals("NOT_FOUND")) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), "No reason code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_REASON_CODE,
          "Survey type", type,
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
    }

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("outcome", outcome);
    root.put("reason", reasonCode);
    root.put("caseId", caseId);
    root.put("eventDate", eventDateTime);

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), OUTCOME_SENT,
            "Survey type", type,
            "Type", ADDRESS_NOT_VALID.toString(),
            "Transaction ID", outcome.getTransactionId().toString(),
            "Routing key", GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    return caseId;
  }
}
