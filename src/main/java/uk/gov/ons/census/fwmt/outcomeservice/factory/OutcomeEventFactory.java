package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.rm.CollectionCase;
import uk.gov.ons.census.fwmt.common.data.rm.Contact;
import uk.gov.ons.census.fwmt.common.data.rm.Event;
import uk.gov.ons.census.fwmt.common.data.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.data.rm.Payload;
import uk.gov.ons.census.fwmt.common.data.rm.Refusal;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.util.HashMap;
import java.util.Map;

@Component
public class OutcomeEventFactory {

  static Map<String, String> noValidHouseholdOutcomeMap = new HashMap<>();

  @Autowired
  public OutcomeEventFactory(BuildSecondaryOutcomeMaps buildSecondaryOutcomeMaps) {
    buildSecondaryOutcomeMaps.buildSecondaryOutcomeMap();
  }

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
    Address address = new Address();

    collectionCase.setId(householdOutcome.getCaseId());
    collectionCase.setAddress(address);
    invalidAddress.setCollectionCase(collectionCase);
    refusal.setCollectionCase(collectionCase);
    payload.setContact(contact);
    payload.setRefusal(refusal);
    payload.setInvalidAddress(invalidAddress);
    payload.setCollectionCase(collectionCase);

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
    case "Non Valid Household":
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
      outcomeEvent.getPayload().getRefusal()
          .setType(noValidHouseholdOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
      outcomeEvent.getPayload().getRefusal().setAgentId(householdOutcome.getUsername());
      outcomeEvent.getPayload().getRefusal().getCollectionCase().setId(householdOutcome.getCaseId());
      break;
    case "Split Address":
      outcomeEvent.getEvent().setType("ADDRESS_NOT_VALID");
      outcomeEvent.getPayload().getInvalidAddress()
          .setReason(noValidHouseholdOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
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
      outcomeEvent.getPayload().getInvalidAddress()
          .setReason(noValidHouseholdOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
      outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());
      break;
    case "Property is a CE - no contact made":
      setCEOutcomeEvent(householdOutcome, outcomeEvent);
      break;
    case "Property is a CE - Contact made":
      setCEOutcomeEvent(householdOutcome, outcomeEvent);
      setCEOutcomeContactMadeEvent(householdOutcome, outcomeEvent);
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Non Valid Household' secondary outcome found: " + householdOutcome.getSecondaryOutcome());
    }
  }

  private void setCEOutcomeEvent(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent) {
    outcomeEvent.getEvent().setType("ADDRESS_TYPE_CHANGED");
    outcomeEvent.getPayload().getCollectionCase().setId(householdOutcome.getCaseId());
    outcomeEvent.getPayload().getCollectionCase().getAddress().setAddressType("CE");
    outcomeEvent.getPayload().getCollectionCase().getAddress()
        .setEstabType(householdOutcome.getCeDetails().getEstablishmentType());
    outcomeEvent.getPayload().getCollectionCase().getAddress()
        .setOrgName(householdOutcome.getCeDetails().getEstablishmentName());
  }

  private void setCEOutcomeContactMadeEvent(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent) {
    if (householdOutcome.getCeDetails().getUsualResidents() > 0) {
      outcomeEvent.getPayload().getCollectionCase().setCeExpectedResponses(householdOutcome.getCeDetails().getUsualResidents());
    }
    outcomeEvent.getPayload().getContact().setTitle(householdOutcome.getCeDetails().getManagerTitle());
    outcomeEvent.getPayload().getContact().setForename(householdOutcome.getCeDetails().getManagerForename());
    outcomeEvent.getPayload().getContact().setSurname(householdOutcome.getCeDetails().getManagerSurname());
    outcomeEvent.getPayload().getContact().setTelNo(householdOutcome.getCeDetails().getContactPhone());
  }
}
