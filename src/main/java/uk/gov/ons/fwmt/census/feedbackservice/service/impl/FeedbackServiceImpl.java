package uk.gov.ons.fwmt.census.feedbackservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.feedbackservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.feedbackservice.message.GatewayFeedbackProducer;
import uk.gov.ons.fwmt.census.feedbackservice.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {

  @Autowired
  private GatewayFeedbackProducer gatewayFeedbackProducer;

  @Override public void sendFeedback(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    gatewayFeedbackProducer.send(censusCaseOutcomeDTO);
  }
}
