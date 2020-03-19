package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component("EXTRAORDINARY_REFUSAL_RECEIVED")
public class SPGExtraordinaryRefusalReceivedProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException {
    String eventDateTime = outcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", outcome);
    root.put("refusalType", "EXTRAORDINARY_REFUSAL");
    root.put("officerId", outcome.getOfficerId());
    root.put("caseId", outcome.getCaseId());
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, spg);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(outcome.getTransactionId()));
    gatewayEventManager.triggerEvent("caseId", CESPG_OUTCOME_SENT,
        "type", "CESPG_EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT",
        "transactionId", outcome.getTransactionId().toString());

    return outcome.getCaseId();
  }
}
