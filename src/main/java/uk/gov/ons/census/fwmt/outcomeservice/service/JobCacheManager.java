package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface JobCacheManager {
  void cacheCCSOutcome(String caseId, CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException;
}
