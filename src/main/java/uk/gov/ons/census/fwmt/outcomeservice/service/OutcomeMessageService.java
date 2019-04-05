package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;

public interface OutcomeMessageService {
  void sendOutcome(OutcomeEvent caseOutcome) throws GatewayException;
}
