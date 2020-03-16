package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSI_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.interview;

@Component
public class InterviewRefusalReceivedProcessor implements InterviewOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSInterviewOutcome ccsIOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Hard refusal", "Extraordinary refusal");
    return validSecondaryOutcomes.contains(ccsIOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSInterviewOutcome ccsIOutcome) throws GatewayException {
    String eventDateTime = ccsIOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("ccsInterviewOutcome", ccsIOutcome);
    root.put("refusalType",
        InterviewSecondaryOutcomeMap.interviewSecondaryOutcomeMap.get(ccsIOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, interview);

    gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent, String.valueOf(ccsIOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(ccsIOutcome.getCaseId()), CCSI_OUTCOME_SENT, "type",
        "CCSI_REFUSAL_RECEIVED_OUTCOME_SENT", "transactionId", ccsIOutcome.getTransactionId().toString(), "Case Ref",
        ccsIOutcome.getCaseReference());
  }
}
