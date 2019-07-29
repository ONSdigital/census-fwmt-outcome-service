package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface InterviewOutcomeServiceProcessor {
  boolean isValid(CCSInterviewOutcome ccsInterviewOutcome);

  void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException;
}
