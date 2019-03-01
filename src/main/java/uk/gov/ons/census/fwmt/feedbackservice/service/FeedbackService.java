package uk.gov.ons.census.fwmt.feedbackservice.service;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.feedbackservice.data.dto.CensusCaseOutcomeDTO;

public interface FeedbackService {
  void sendFeedback(CensusCaseOutcomeDTO caseOutcome) throws GatewayException;
}
