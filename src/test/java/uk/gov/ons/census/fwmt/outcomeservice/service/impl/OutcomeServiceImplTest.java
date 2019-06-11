package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.Contact;
import uk.gov.ons.census.fwmt.common.data.rm.Event;
import uk.gov.ons.census.fwmt.common.data.rm.Fulfillment;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.data.rm.Payload;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.factory.FulfilmentRequestFactory;
import uk.gov.ons.census.fwmt.outcomeservice.factory.OutcomeEventFactory;
import uk.gov.ons.census.fwmt.outcomeservice.helper.HouseholdOutcomeBuilder;
import uk.gov.ons.census.fwmt.outcomeservice.helper.OutcomeEventFactoryBuilder;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;

@RunWith(MockitoJUnitRunner.class)
public class OutcomeServiceImplTest {
  @InjectMocks
  private OutcomeServiceImpl outcomeServiceImpl;

  @Mock
  private OutcomeEventFactory outcomeEventFactory;

  @Mock
  private GatewayEventManager gatewayEventManager;

  @Mock
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Mock
  private FulfilmentRequestFactory fulfilmentRequestFactory;

  @Test
  public void createHouseHoldOutcomeEvent() throws GatewayException {
    String productCode = "P_OR_H1";
    List<OutcomeEvent> outcomeEventList = new ArrayList<>();
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperH();

    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Payload payload = new Payload();
    Fulfillment fulfillment = new Fulfillment();
    Contact contact = new Contact();

    fulfillment.setContact(contact);
    fulfillment.setCaseId(householdOutcome.getCaseId());
    payload.setFulfillment(fulfillment);

    outcomeEvent.setPayload(payload);

    Event event = new Event();
    event.setType("FULFILMENT_REQUESTED");
    event.setSource("FIELDWORK_GATEWAY");
    event.setChannel("FIELD");
    event.setTransactionId(householdOutcome.getTransactionId());
    event.setDateTime(householdOutcome.getEventDate());
    outcomeEvent.setEvent(event);

    //Product product = getPackCodeFromQuestionnaireType(fulfillmentRequest);

//
//    Mockito.when(outcomeEventFactory.createOutcomeEvent(any(HouseholdOutcome.class))).thenReturn(outcomeEvent);
//
//    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);
//
//    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(OUTCOME_SENT_RM), any());

   // Mockito.verify(gatewayOutcomeProducer).sendFulfilmentRequest(outcomeEvents);
  }

  @Test
  public void createInvalidHouseHoldOutcomeEvent() throws GatewayException {
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdDerelict();

    OutcomeEvent outcomeEvent = new OutcomeEventFactoryBuilder().createOutcomeEventForHousehold(householdOutcome);

    Mockito.when(outcomeEventFactory.createOutcomeEvent(any(HouseholdOutcome.class))).thenReturn(outcomeEvent);

    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(OUTCOME_SENT_RM), any());
  }

  @Test
  public void createHouseHoldRefusalOutcomeEvent() throws GatewayException {
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdHardRefusal();

    OutcomeEvent outcomeEvent = new OutcomeEventFactoryBuilder().createOutcomeEventForHouseholdRefusal(householdOutcome);

    Mockito.when(outcomeEventFactory.createOutcomeEvent(any(HouseholdOutcome.class))).thenReturn(outcomeEvent);

    outcomeServiceImpl.createHouseHoldOutcomeEvent(householdOutcome);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(OUTCOME_SENT_RM), any());
  }
}