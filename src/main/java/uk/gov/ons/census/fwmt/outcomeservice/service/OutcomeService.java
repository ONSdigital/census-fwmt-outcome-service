package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface OutcomeService {
  void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException;

  void createPropertyListingOutcomeEvent(CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException;

  void createInterviewOutcomeEvent(CCSInterviewOutcome ccsInterviewOutcome);

  void createSpgOutcomeEvent(SPGOutcome spgOutcome) throws GatewayException;
}
