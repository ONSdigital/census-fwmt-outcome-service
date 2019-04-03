package uk.gov.ons.census.fwmt.outcomeservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;

public interface CometTranslationService {

  void transformCometPayload(HouseholdOutcome householdOutcome) throws GatewayException;

}
