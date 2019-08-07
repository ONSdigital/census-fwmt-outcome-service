package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface HHOutcomeServiceProcessor {
  boolean isValid(HouseholdOutcome householdOutcome);

  void processMessage(HouseholdOutcome householdOutcome);
}
