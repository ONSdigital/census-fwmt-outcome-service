package uk.gov.ons.census.fwmt.outcomeservice.helper;

import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.CollectionCase;
import uk.gov.ons.census.fwmt.common.data.rm.Contact;
import uk.gov.ons.census.fwmt.common.data.rm.Event;
import uk.gov.ons.census.fwmt.common.data.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.data.rm.Payload;
import uk.gov.ons.census.fwmt.common.data.rm.Refusal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OutcomeEventFactoryBuilder {

  static Map<String, String> noValidHouseholdOutcomeMap = new HashMap<>();

  public OutcomeEvent createOutcomeEventForHousehold(HouseholdOutcome householdOutcome) {
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

    outcomeEvent.getEvent().setType("ADDRESS_NOT_VALID");
    outcomeEvent.getPayload().getInvalidAddress()
        .setReason(noValidHouseholdOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());

    return outcomeEvent;
  }

  public OutcomeEvent createOutcomeEventForHouseholdRefusal(HouseholdOutcome householdOutcome) {
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

    outcomeEvent.getEvent().setType("REFUSAL_RECEIVED");
    outcomeEvent.getPayload().getInvalidAddress()
        .setReason(noValidHouseholdOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());

    return outcomeEvent;
  }
}
