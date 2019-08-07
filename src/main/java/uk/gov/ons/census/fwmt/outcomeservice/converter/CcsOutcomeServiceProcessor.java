package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface CcsOutcomeServiceProcessor {
  boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome);

  void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome);
}
