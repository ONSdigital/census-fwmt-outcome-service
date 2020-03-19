package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.UUID;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;

public interface SPGOutcomeServiceProcessor {
  UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException;
}
