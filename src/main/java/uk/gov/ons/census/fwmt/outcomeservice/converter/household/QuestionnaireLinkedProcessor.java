package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class QuestionnaireLinkedProcessor implements OutcomeServiceProcessor {

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
  public void processMessage(HouseholdOutcome householdOutcome) {
    for (FulfillmentRequest fulfillmentRequest : householdOutcome.getFulfillmentRequests()) {
      if (isQuestionnaireLinked(fulfillmentRequest)) {
        Map<String, Object> root = new HashMap<>();
        root.put("householdOutcome", householdOutcome);
        root.put("questionnaireId", fulfillmentRequest.getQuestionnaireId());

        String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, household);

        try {
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
          gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
        } catch (GatewayException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId() != null);
  }
}
