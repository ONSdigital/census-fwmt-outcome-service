package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;

public interface OutcomeService {
  void CreateHouseHoldOutComeEvent(HouseholdOutcome householdOutcome) throws GatewayException;
  void sendOutcome(OutcomeEvent caseOutcome) throws GatewayException;
}
