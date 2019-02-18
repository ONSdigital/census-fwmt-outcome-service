package uk.gov.ons.fwmt.census.feedbackservice.service;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.feedbackservice.data.dto.CensusCaseOutcomeDTO;

public interface FeedbackService {
  void sendFeedback(CensusCaseOutcomeDTO caseOutcome) throws GatewayException;
}
