package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  public void sendOutcome(OutcomeEvent outcomeEvent) throws GatewayException {
    gatewayEventManager
        .triggerEvent(outcomeEvent.getEvent().getTransactionId(), COMET_OUTCOME_RECEIVED, LocalTime.now());
    gatewayOutcomeProducer.send(outcomeEvent);
    gatewayEventManager.triggerEvent(outcomeEvent.getEvent().getTransactionId(), OUTCOME_SENT_RM, LocalTime.now());
  }
}
