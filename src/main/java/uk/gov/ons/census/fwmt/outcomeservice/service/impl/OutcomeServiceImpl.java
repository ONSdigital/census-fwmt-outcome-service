package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.factory.FulfilmentRequestFactory;
import uk.gov.ons.census.fwmt.outcomeservice.factory.OutcomeEventFactory;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomeEventFactory outcomeEventFactory;

  @Autowired
  private FulfilmentRequestFactory fulfilmentRequestFactory;

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    if (householdOutcome.getFulfilmentRequests().size() >= 1 && householdOutcome.getFulfilmentRequests() != null) {

      OutcomeEvent[] processedFulfilmentRequests = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

      for (OutcomeEvent outcomeEvent : processedFulfilmentRequests) {

        if (outcomeEvent.getEvent().getType().equals("FULFILMENT_REQUESTED")) {
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent);
        }
      }

    } else if (householdOutcome.getFulfilmentRequests() == null) {

      OutcomeEvent outcomeEvent = outcomeEventFactory.createOutcomeEvent(householdOutcome);

      if (outcomeEvent.getEvent().getType().equals("ADDRESS_NOT_VALID")) {
        gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent);

      } else if (outcomeEvent.getEvent().getType().equals("REFUSAL_RECEIVED")) {
        gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent);
      }

      gatewayEventManager
          .triggerEvent(String.valueOf(outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().getId()),
              OUTCOME_SENT_RM, LocalTime.now());
    }
  }
}
