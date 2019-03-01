package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.CensusCaseOutcomeDTO;

public interface OutcomeService {
  void sendOutcome(CensusCaseOutcomeDTO caseOutcome) throws GatewayException;
}
