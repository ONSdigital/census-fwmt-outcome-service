package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("EXTRAORDINARY_REFUSAL_RECEIVED")
public class SpgExtraordinaryRefusalReceivedProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  DateFormat dateFormat;
  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;
  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    UUID caseId = (outcome.getCaseId() != null) ? outcome.getCaseId() : caseIdHolder;
    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", outcome);
    root.put("refusalType", "EXTRAORDINARY_REFUSAL");
    root.put("officerId", outcome.getOfficerId());
    root.put("caseId", caseId);
    root.put("eventDate", eventDateTime);

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, spg);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(caseId), CESPG_OUTCOME_SENT,
        "type", "CESPG_EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT",
        "transactionId", outcome.getTransactionId().toString());

    return caseId;
  }
}
