package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.household.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HHOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component
public class QuestionnaireLinkedProcessorHH implements HHOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    List<String> invalidSecondaryOutcomes = Arrays
        .asList("Split address", "Hard refusal", "Extraordinary refusal");
    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && !invalidSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException{
    for (FulfilmentRequest fulfilmentRequest : householdOutcome.getFulfilmentRequests()) {
      if (isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = householdOutcome.getEventDate().toString();

        Map<String, Object> root = new HashMap<>();
        root.put("householdOutcome", householdOutcome);
        root.put("questionnaireId", fulfilmentRequest.getQuestionnaireID());
        root.put("eventDate", eventDateTime + "Z");

        String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, household);

        gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
        gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, new HashMap<>( Map.of("type", "HH_QUESTIONNAIRE_LINKED_OUTCOME_SENT")));
      }
    }
  }

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireID() != null);
  }
}
