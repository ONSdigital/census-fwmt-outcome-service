package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;

@Component
public class InterviewRefusalReceivedProcessor implements InterviewOutcomeServiceProcessor {
  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    return false;
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) {

  }
}
