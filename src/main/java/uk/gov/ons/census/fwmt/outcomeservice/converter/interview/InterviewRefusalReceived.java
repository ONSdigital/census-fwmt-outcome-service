package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;

public class InterviewRefusalReceived implements InterviewOutcomeServiceProcessor {
  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    return false;
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) {

  }
}
