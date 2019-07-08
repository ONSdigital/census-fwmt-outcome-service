package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.factory.FulfilmentRequestFactory;
import uk.gov.ons.census.fwmt.outcomeservice.factory.OutcomeEventFactory;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;

//@RunWith(MockitoJUnitRunner.class)
public class OutcomeServiceImplTest {
//  @InjectMocks
//  private OutcomeServiceImpl outcomeServiceImpl;
//
//  @Mock
//  private OutcomeEventFactory outcomeEventFactory;
//
//  @Mock
//  private GatewayEventManager gatewayEventManager;
//
//  @Mock
//  private GatewayOutcomeProducer gatewayOutcomeProducer;
//
//  @Mock
//  private FulfilmentRequestFactory fulfilmentRequestFactory;
//
//  @Mock
//  private HouseholdOutcome householdOutcome;

//  @Test
//  public void createHouseHoldOutcomeEvent() throws GatewayException {
//    String productCode = "P_OR_H1";
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperH();
//    List<OutcomeEvent> outcomeEventList = new ArrayList<>();
//
//    OutcomeEvent outcomeEvent = new OutcomeEvent();
//    Payload payload = new Payload();
//    Fulfillment fulfillment = new Fulfillment();
//    Contact contact = new Contact();
//
//    fulfillment.setContact(contact);
//    fulfillment.setCaseId(householdOutcome.getCaseId());
//    payload.setFulfillment(fulfillment);
//
//    outcomeEvent.setPayload(payload);
//
//    Event event = new Event();
//    event.setType("FULFILMENT_REQUESTED");
//    event.setSource("FIELDWORK_GATEWAY");
//    event.setChannel("FIELD");
//    event.setTransactionId(householdOutcome.getTransactionId());
//    event.setDateTime(householdOutcome.getEventDate());
//    outcomeEvent.setEvent(event);
//
//    outcomeEventList.add(outcomeEvent);
//
//    Mockito.when(fulfilmentRequestFactory.createFulfilmentEvents(any(HouseholdOutcome.class))).thenReturn(outcomeEventList.toArray(new OutcomeEvent[0]));
//
//    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);
//
//    Mockito.verify(gatewayOutcomeProducer).sendFulfilmentRequest(outcomeEvent);
//  }
//
//  @Test
//  public void createInvalidHouseHoldOutcomeEvent() throws GatewayException {
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdDerelict();
//
//    OutcomeEvent outcomeEvent = new OutcomeEventFactoryBuilder().createOutcomeEventForHousehold(householdOutcome);
//
//    Mockito.when(outcomeEventFactory.createOutcomeEvent(any(HouseholdOutcome.class))).thenReturn(outcomeEvent);
//
//    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);
//
//    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(OUTCOME_SENT_RM), any());
//  }
//
//  @Test
//  public void createHouseHoldRefusalOutcomeEvent() throws GatewayException {
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdHardRefusal();
//
//    OutcomeEvent outcomeEvent = new OutcomeEventFactoryBuilder().createOutcomeEventForHouseholdRefusal(householdOutcome);
//
//    Mockito.when(outcomeEventFactory.createOutcomeEvent(any(HouseholdOutcome.class))).thenReturn(outcomeEvent);
//
//    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);
//
//    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(OUTCOME_SENT_RM), any());
//  }
}