package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface OutcomeService {
  void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException;

  void createPropertyListingOutcomeEvent(CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException;

  <T> void ccsPropertyListingOutcomeEvent(T outcome);
}
