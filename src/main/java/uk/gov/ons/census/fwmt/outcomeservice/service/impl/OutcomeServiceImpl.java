package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.CollectionCase;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;
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

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    OutcomeEvent outcomeEvent = newOutcomeEvent(householdOutcome);

    // pass in routing key to send method instead and let one send method do the heavy lifting
    if (householdOutcome.getPrimaryOutcome().equals("No Valid Household")) {
      gatewayOutcomeProducer.sendNonValidHouseholdOutcome(outcomeEvent);

    } else if (householdOutcome.getPrimaryOutcome().equals("Contact Made")) {
      gatewayOutcomeProducer.sendTest(outcomeEvent);
    }

    gatewayEventManager
        .triggerEvent(String.valueOf(outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().getId()),
            OUTCOME_SENT_RM, LocalTime.now());
  }

  private OutcomeEvent newOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Payload payload = new Payload();
    InvalidAddress invalidAddress = new InvalidAddress();
    CollectionCase collectionCase = new CollectionCase();

    collectionCase.setId(householdOutcome.getCaseId());
    invalidAddress.setCollectionCase(collectionCase);
    payload.setInvalidAddress(invalidAddress);
    outcomeEvent.setPayload(payload);

    buildOutcome(householdOutcome, outcomeEvent);

    Event event = new Event();
    event.setTransactionId(householdOutcome.getTransactionId());

    outcomeEvent.setEvent(event);
    return outcomeEvent;
  }

  private void buildOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      getSecondaryNoValidHouseholdOutcome(householdOutcome, outcomeEvent);
      break;
    case "Contact Made":
      outcomeEvent.getPayload().getInvalidAddress().setReason("Contact Made");
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "No primary outcome found: {}",
          householdOutcome.getPrimaryOutcome());
    }
  }

  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "Derelict":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Demolished":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Can't Find":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Unaddressable Object":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Non-residential":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Duplicate":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    case "Under Construction":
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "No noValidHousehold secondary outcome found: {}",
          householdOutcome.getSecondaryOutcome());
    }
  }
}
