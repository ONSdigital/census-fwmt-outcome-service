package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface OutcomeService {
  void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException;
}
