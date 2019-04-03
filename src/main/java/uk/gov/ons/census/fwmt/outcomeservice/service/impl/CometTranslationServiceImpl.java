package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.CollectionCase;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;
import uk.gov.ons.census.fwmt.outcomeservice.service.CometTranslationService;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import static uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress.ReasonEnum.DERELICT;

@Service
public class CometTranslationServiceImpl implements CometTranslationService {

  @Autowired
  private OutcomeService outcomeService;

  public void transformCometPayload(HouseholdOutcome householdOutcome) throws GatewayException {

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
    event.setTransactionId("c45de4dc-3c3b-11e9-b210-d663bd873d93");

    outcomeEvent.setEvent(event);
    outcomeService.sendOutcome(outcomeEvent);
  }

  private void buildOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      getSecondaryNoValidHouseholdOutcome(householdOutcome, outcomeEvent);
      break;
    case "Contact Made":
      // code block
      break;
    default:
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "No primary outcome found: {}", householdOutcome.getPrimaryOutcome());
    }
  }

  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "derelict":
      outcomeEvent.getPayload().getInvalidAddress().setReason(DERELICT);
      break;
    case "demolished":
      // code block
      break;
    default:
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "No secondary outcome found: {}", householdOutcome.getSecondaryOutcome());
    }
  }
}
