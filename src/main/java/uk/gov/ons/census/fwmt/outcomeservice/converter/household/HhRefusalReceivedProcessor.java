package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HhOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class HhRefusalReceivedProcessor implements HhOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Hard refusal", "Extraordinary refusal");
    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException {
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = householdOutcome.getEventDate().toString();

    root.put("householdOutcome", householdOutcome);
    root.put("refusalType",
        HhSecondaryOutcomeMap.householdSecondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    if (householdOutcome.getFulfillmentRequests() != null) {
      root.put("title", householdOutcome.getFulfillmentRequests().get(0).getRequesterTitle());
      root.put("forename", householdOutcome.getFulfillmentRequests().get(0).getRequesterForename());
      root.put("surname", householdOutcome.getFulfillmentRequests().get(0).getRequesterSurname());
      root.put("telNo", householdOutcome.getFulfillmentRequests().get(0).getRequesterPhone());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, household);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, "type",
        "HH_REFUSAL_RECEIVED_OUTCOME_SENT", "transactionId", householdOutcome.getTransactionId().toString(), "Case Ref",
        householdOutcome.getCaseReference());
  }

}
