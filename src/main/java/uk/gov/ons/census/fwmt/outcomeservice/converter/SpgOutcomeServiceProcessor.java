package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;

import java.util.UUID;

public interface SpgOutcomeServiceProcessor {
  UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException;
}
