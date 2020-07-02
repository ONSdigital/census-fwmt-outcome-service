package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;

public interface OutcomeService {
  void createSpgOutcomeEvent(SpgOutcomeSuperSetDto outcome) throws GatewayException;

  void createCeOutcomeEvent(SpgOutcomeSuperSetDto outcome) throws GatewayException;
}
