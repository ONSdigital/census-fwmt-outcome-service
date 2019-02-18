package uk.gov.ons.fwmt.census.feedbackservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;
import uk.gov.ons.fwmt.census.feedbackservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.feedbackservice.message.GatewayFeedbackProducer;
import uk.gov.ons.fwmt.census.feedbackservice.service.FeedbackService;

import static uk.gov.ons.fwmt.census.feedbackservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;

@Service
public class FeedbackServiceImpl implements FeedbackService {

  @Autowired
  private GatewayFeedbackProducer gatewayFeedbackProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override public void sendFeedback(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    gatewayFeedbackProducer.send(censusCaseOutcomeDTO);
    gatewayEventManager.triggerEvent(censusCaseOutcomeDTO.getCaseId(), COMET_OUTCOME_RECEIVED);
  }
}
