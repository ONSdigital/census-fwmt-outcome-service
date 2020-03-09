package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface SpgOutcomeServiceProcessor {

  boolean isValid(SPGOutcome spgOutcome);

  void processMessage(SPGOutcome spgOutcome) throws GatewayException;
}
