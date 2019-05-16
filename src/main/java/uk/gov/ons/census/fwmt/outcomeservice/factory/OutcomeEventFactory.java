package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.CollectionCase;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Contact;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Refusal;

@Component
public class OutcomeEventFactory {

  public OutcomeEvent createOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    return newOutcomeEvent(householdOutcome);
  }

  private OutcomeEvent newOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Payload payload = new Payload();
    InvalidAddress invalidAddress = new InvalidAddress();
    Refusal refusal = new Refusal();
    Contact contact = new Contact();
    CollectionCase collectionCase = new CollectionCase();

    collectionCase.setId(householdOutcome.getCaseId());
    invalidAddress.setCollectionCase(collectionCase);
    refusal.setCollectionCase(collectionCase);
    payload.setContact(contact);
    payload.setRefusal(refusal);
    payload.setInvalidAddress(invalidAddress);

    outcomeEvent.setPayload(payload);

    Event event = new Event();
    event.setSource("FIELDWORK_GATEWAY");
    event.setChannel("FIELD");
    event.setTransactionId(householdOutcome.getTransactionId());
    event.setDateTime(householdOutcome.getEventDate());
    outcomeEvent.setEvent(event);

    buildOutcome(householdOutcome, outcomeEvent);

    return outcomeEvent;
  }

  private void buildOutcome(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      getSecondaryNoValidHouseholdOutcome(householdOutcome, outcomeEvent);
      break;
    case "Contact Made":
      getSecondaryContactMadeOutcome(householdOutcome, outcomeEvent);
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Primary' outcome found: " + householdOutcome.getPrimaryOutcome());
    }
  }

  private void getSecondaryContactMadeOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "Hard Refusal":
    case "Extraordinary Refusal":
      outcomeEvent.getEvent().setType("REFUSAL_RECEIVED");
      outcomeEvent.getPayload().getRefusal().setType(householdOutcome.getSecondaryOutcome());
      outcomeEvent.getPayload().getRefusal().setAgentId(householdOutcome.getUsername());
      outcomeEvent.getPayload().getRefusal().getCollectionCase().setId(householdOutcome.getCaseId());
      break;
    case "Split Address":
      outcomeEvent.getEvent().setType("ADDRESS_NOT_VALID");
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Contact Made' secondary outcome found: " + householdOutcome.getSecondaryOutcome());
    }
  }

  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) throws GatewayException {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "Derelict":
    case "Demolished":
    case "Can't Find":
    case "Unaddressable Object":
    case "Non-residential":
    case "Duplicate":
    case "Under Construction":
      outcomeEvent.getEvent().setType("ADDRESS_NOT_VALID");
      outcomeEvent.getPayload().getInvalidAddress().setReason(householdOutcome.getSecondaryOutcome());
      outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Non Valid Household' secondary outcome found: " + householdOutcome.getSecondaryOutcome());
    }
  }
}
