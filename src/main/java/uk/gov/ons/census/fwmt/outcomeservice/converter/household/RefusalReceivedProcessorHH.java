package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HHOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component
public class RefusalReceivedProcessorHH implements HHOutcomeServiceProcessor {

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
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException{
    HouseholdSecondaryOutcomeMap householdSecondaryOutcomeMap = new HouseholdSecondaryOutcomeMap();
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = householdOutcome.getEventDate().toString();

    root.put("householdOutcome", householdOutcome);
    root.put("refusalType", householdSecondaryOutcomeMap.householdSecondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    if (householdOutcome.getFulfilmentRequests() != null) {
      root.put("title", householdOutcome.getFulfilmentRequests().get(0).getRequesterTitle());
      root.put("forename", householdOutcome.getFulfilmentRequests().get(0).getRequesterForename());
      root.put("surname", householdOutcome.getFulfilmentRequests().get(0).getRequesterSurname());
      root.put("telNo", householdOutcome.getFulfilmentRequests().get(0).getRequesterPhone());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, household);

    gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, new HashMap<>( Map.of("type", "HH_REFUSAL_RECEIVED_OUTCOME_SENT")));
  }


}
