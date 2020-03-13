package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.NEW_UNIT_ADDRESS;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("NEW_UNIT_ADDRESS")
public class SpgNewUnitAddressLinked implements SpgOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessage(CommonOutcome spgOutcome) throws GatewayException {
    UUID newRandomUUID = UUID.randomUUID();
    String eventDateTime = spgOutcome.getEventDate().toString();

    Map<String, Object> root = new HashMap<>();
    root.put("generatedUuid", newRandomUUID);
    root.put("siteCaseId", "");
    root.put("eventDate", eventDateTime + "Z");
    root.put("agentId", spgOutcome.getOfficerId());
    root.put("coordinatorId", spgOutcome.getCoordinatorId());
    root.put("addressLine1", spgOutcome.getAddress().getAddressLine1());

    String outcomeEvent = TemplateCreator.createOutcomeMessage(NEW_UNIT_ADDRESS, root, spg);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(newRandomUUID), CESPG_OUTCOME_SENT,
        "type", "CESPG_REFUSAL_RECEIVED_OUTCOME_SENT", "transactionId", spgOutcome.getTransactionId().toString());
  }
}
