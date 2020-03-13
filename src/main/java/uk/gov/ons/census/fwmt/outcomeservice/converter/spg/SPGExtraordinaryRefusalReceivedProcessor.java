package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("EXTRAORDINARY_REFUSAL_RECEIVED")
public class SPGExtraordinaryRefusalReceivedProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessage(CommonOutcome spgOutcome) throws GatewayException {
    String eventDateTime = spgOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", spgOutcome);
    root.put("generatedUuid", "caseId");
    root.put("eventDate", eventDateTime + "Z");
    root.put("agentId", spgOutcome.getOfficerId());
    root.put("refusalType", "EXTRAORDINARY_REFUSAL");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, spg);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent("caseId", CESPG_OUTCOME_SENT,
        "type", "CESPG_EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT", "transactionId", spgOutcome.getTransactionId().toString());
  }
}
