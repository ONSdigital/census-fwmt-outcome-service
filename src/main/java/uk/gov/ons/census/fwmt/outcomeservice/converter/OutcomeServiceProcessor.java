package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.util.UUID;

public interface OutcomeServiceProcessor {
  UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException;
}
