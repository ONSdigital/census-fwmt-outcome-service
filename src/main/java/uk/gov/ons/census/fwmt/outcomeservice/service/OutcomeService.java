package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

public interface OutcomeService {
  void createSpgOutcomeEvent(CommonOutcome outcome) throws GatewayException;

  void createCeOutcomeEvent(CommonOutcome outcome) throws GatewayException;

  void createHhOutcomeEvent(CommonOutcome outcome) throws GatewayException;

  void createCcsPropertyListingOutcomeEvent(CommonOutcome outcome) throws GatewayException;

  void createCcsInterviewOutcomeEvent(CommonOutcome outcome) throws GatewayException;

  void createNcOutcomeEvent(CommonOutcome outcome) throws GatewayException;
}
