package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.FulfilmentRequest;
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

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_IS_NULL;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class HhQuestionnaireLinkedProcessor implements HhOutcomeServiceProcessor {

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
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException {
    if (householdOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
          householdOutcome.getCaseReference(), FAILED_FULFILMENT_REQUEST_IS_NULL,
          "Primary Outcome", householdOutcome.getPrimaryOutcome(), "Secondary Outcome",
          householdOutcome.getSecondaryOutcome());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : householdOutcome.getFulfillmentRequests()) {
      if (isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = householdOutcome.getEventDate().toString();

        Map<String, Object> root = new HashMap<>();
        root.put("householdOutcome", householdOutcome);
        root.put("questionnaireId", fulfilmentRequest.getQuestionnaireID());
        root.put("eventDate", eventDateTime + "Z");

        String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, household);

        gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()),
            GatewayOutcomeQueueConfig.GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY);
        gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, "type",
            "HH_QUESTIONNAIRE_LINKED_OUTCOME_SENT", "transactionId", householdOutcome.getTransactionId().toString(),
            "Case Ref", householdOutcome.getCaseReference());
      }
    }
  }

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }
}
