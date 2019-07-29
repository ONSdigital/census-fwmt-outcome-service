package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NO_CONTACT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.interview;

@Component
public class InterviewQuestionnaireLinkedProcessor implements InterviewOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSInterviewOutcome ccsInterviewOutcome) {
    return isContactMade(ccsInterviewOutcome) || isNoContact(ccsInterviewOutcome);
  }

  @Override
  public void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException{
    if (isQuestionnaireLinked(ccsInterviewOutcome.getFulfillmentRequest())) {
      Map<String, Object> root = new HashMap<>();
      root.put("ccsInterviewOutcome", ccsInterviewOutcome);
      root.put("questionnaireId", ccsInterviewOutcome.getFulfillmentRequest().getQuestionnaireId());

      String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, interview);

      gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(ccsInterviewOutcome.getTransactionId()));
      gatewayEventManager.triggerEvent(String.valueOf(ccsInterviewOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
    }
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId() != null);
  }

  private boolean isContactMade(CCSInterviewOutcome ccsInterviewOutcome) {
    List<String> validCompletion = Collections.singletonList("Left questionnaire on final visit");
    return ccsInterviewOutcome.getPrimaryOutcome().equals(NO_CONTACT.toString()) && validCompletion
        .contains(ccsInterviewOutcome.getSecondaryOutcome());
  }

  private boolean isNoContact(CCSInterviewOutcome ccsInterviewOutcome) {
    List<String> validCompletion = Arrays.asList("Complete on paper (full)", "Complete on paper (partial)",
        "Left questionnaire on final visit");
    return ccsInterviewOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validCompletion
        .contains(ccsInterviewOutcome.getSecondaryOutcome());
  }
}
