package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override public void sendOutcome(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    gatewayOutcomeProducer.send(censusCaseOutcomeDTO);
    gatewayEventManager.triggerEvent(censusCaseOutcomeDTO.getCaseId(), COMET_OUTCOME_RECEIVED);
  }
}
