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
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class InterviewAddressNotValidProcessor implements InterviewOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSInterviewOutcome ccsInterviewOutcome) {
    return isNonValid(ccsInterviewOutcome) || isIncorrectAddress(ccsInterviewOutcome);
  }

  @Override
  public void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    Map<String, Object> root = new HashMap<>();
    root.put("ccsInterviewOutcome", ccsInterviewOutcome);
    root.put("secondaryOutcome",
        InterviewSecondaryOutcomeMap.interviewSecondaryOutcomeMap.get(ccsInterviewOutcome.getSecondaryOutcome()));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, household);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(ccsInterviewOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(ccsInterviewOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
  }

  private boolean isNonValid(CCSInterviewOutcome ccsInterviewOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Derelict", "Demolished", "Property is a CE", "Non residential or business",
            "Duplicate", "Under construction", "Incorrect address", "Property is a household");
    return ccsInterviewOutcome.getPrimaryOutcome().equals(NOT_VALID.toString()) && validSecondaryOutcomes
        .contains(ccsInterviewOutcome.getSecondaryOutcome());
  }

  private boolean isIncorrectAddress(CCSInterviewOutcome ccsInterviewOutcome) {
    List<String> validSecondaryOutcomes = Arrays.asList("Split address", "Property not in postcode boundary",
        "CE out of scope");
    return ccsInterviewOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validSecondaryOutcomes
        .contains(ccsInterviewOutcome.getSecondaryOutcome());
  }
}
