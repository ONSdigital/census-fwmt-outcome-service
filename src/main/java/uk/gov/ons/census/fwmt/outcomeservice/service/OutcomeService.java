package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

public interface OutcomeService {
  void createSpgOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;

  void createCeOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;

  void createHhOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;

  void createCcsPropertyListingOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;

  void createCcsInterviewOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;

  void createNcOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException;
}
