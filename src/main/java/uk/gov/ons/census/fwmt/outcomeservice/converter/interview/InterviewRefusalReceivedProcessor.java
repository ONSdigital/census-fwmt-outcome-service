package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.interview;

@Component
public class InterviewRefusalReceivedProcessor implements InterviewOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSInterviewOutcome ccsInterviewOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Hard refusal", "Extraordinary refusal");
    return validSecondaryOutcomes.contains(ccsInterviewOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    Map<String, Object> root = new HashMap<>();
    root.put("ccsInterviewOutcome", ccsInterviewOutcome);
    root.put("refusalType",
        InterviewSecondaryOutcomeMap.interviewSecondaryOutcomeMap.get(ccsInterviewOutcome.getSecondaryOutcome()));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, interview);

    gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent, String.valueOf(ccsInterviewOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(ccsInterviewOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
  }
}
