package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;

public interface InterviewOutcomeServiceProcessor {
  boolean isValid(HouseholdOutcome householdOutcome);

  void processMessage(HouseholdOutcome householdOutcome);
}
