package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.FulfillmentRequest;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Contact;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Fulfillment;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Uac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FulfilmentRequestFactory {

  public static Map<String, String> householdPaperMap = new HashMap<>();
  public static Map<String, String> householdContinuationMap = new HashMap<>();
  public static Map<String, String> householdIndividualMap = new HashMap<>();
  public static Map<String, String> householdUacMap = new HashMap<>();
  public static Map<String, String> individualUacMap = new HashMap<>();

  private BuildFulfilmentRequestMaps buildFulfilmentRequestMaps;

  public FulfilmentRequestFactory(
      BuildFulfilmentRequestMaps buildFulfilmentRequestMaps) {
    this.buildFulfilmentRequestMaps = buildFulfilmentRequestMaps;
    this.buildFulfilmentRequestMaps.buildHouseholdPaperRequestMap();
    this.buildFulfilmentRequestMaps.buildIndividualPaperRequestMap();
    this.buildFulfilmentRequestMaps.buildHouseholdContinuationPaperRequestMap();
    this.buildFulfilmentRequestMaps.buildHouseholdUacRequestMap();
    this.buildFulfilmentRequestMaps.buildIndividualUacRequestMap();
  }

  public OutcomeEvent[] createFulfilmentEvents(HouseholdOutcome householdOutcome) throws GatewayException {
    return getFulfilmentRequest(householdOutcome);
  }

  private OutcomeEvent buildFulfilmentPayload(HouseholdOutcome householdOutcome) {
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

    return outcomeEvent;
  }

  private OutcomeEvent buildUacPayload(HouseholdOutcome householdOutcome) {
    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Payload payload = new Payload();
    Uac uac = new Uac();


    uac.setCaseId(householdOutcome.getCaseId());
    payload.setUac(uac);

    outcomeEvent.setPayload(payload);

    Event event = new Event();
    event.setType("QUESTIONNAIRE_LINKED");
    event.setSource("FIELDWORK_GATEWAY");
    event.setChannel("FIELD");
    event.setTransactionId(householdOutcome.getTransactionId());
    event.setDateTime(householdOutcome.getEventDate());
    outcomeEvent.setEvent(event);

    return outcomeEvent;
  }

  private OutcomeEvent[] getFulfilmentRequest(HouseholdOutcome householdOutcome)
      throws GatewayException {
    List<OutcomeEvent> outcomeEventList = new ArrayList<>();
    for (FulfillmentRequest fulfillmentRequest : householdOutcome.getFulfillmentRequests()) {
      OutcomeEvent outcomeEvent;
      switch (householdOutcome.getSecondaryOutcome()) {
      case "Paper Questionnaire required by post":
        outcomeEvent = buildFulfilmentPayload(householdOutcome);
        outcomeEventList.add(getQuestionnaireByPost(outcomeEvent, fulfillmentRequest));
        break;
      case "UAC required by text":
        outcomeEvent = buildFulfilmentPayload(householdOutcome);
        outcomeEventList.add(getUacByText(outcomeEvent, fulfillmentRequest));
        break;
      case "Paper H Questionnaire issued":
      case "Will Complete":
      case "Have Completed":
      case "Collected completed questionnaire":
      case "Call back another time":
      case "Holiday home":
      case "Second residence":
      case "Requested assistance":
        outcomeEvent = buildUacPayload(householdOutcome);
        outcomeEvent.getPayload().getUac().setQuestionnaireId(fulfillmentRequest.getQuestionnaireId());
        outcomeEventList.add(outcomeEvent);
        break;
      default:
          throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              "Failed to find valid Secondary Outcome " + householdOutcome.getSecondaryOutcome());
        }
      }
    return outcomeEventList.toArray(new OutcomeEvent[0]);
  }

  private OutcomeEvent getQuestionnaireByPost(OutcomeEvent outcomeEvent,
      FulfillmentRequest fulfillmentRequest) throws GatewayException {

    if (householdPaperMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdPaperMap.get(fulfillmentRequest.getQuestionnaireType()));

    } else if (householdContinuationMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdContinuationMap.get(fulfillmentRequest.getQuestionnaireType()));

    } else if (householdIndividualMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdIndividualMap.get(fulfillmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfillment().getContact().setTitle(fulfillmentRequest.getRequesterTitle());
      outcomeEvent.getPayload().getFulfillment().getContact().setForename(fulfillmentRequest.getRequesterForename());
      outcomeEvent.getPayload().getFulfillment().getContact().setSurname(fulfillmentRequest.getRequesterSurname());

    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to find valid Questionnaire Type " + fulfillmentRequest.getQuestionnaireType());
    }
    return outcomeEvent;
  }

  private OutcomeEvent getUacByText(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest) throws GatewayException {
    if (householdUacMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdUacMap.get(fulfillmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfillment().getContact().setTelNo(fulfillmentRequest.getRequesterPhone());

    } else if (individualUacMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(individualUacMap.get(fulfillmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfillment().getContact().setTelNo(fulfillmentRequest.getRequesterPhone());

    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to find valid Questionnaire Type " + fulfillmentRequest.getQuestionnaireType());
    }
    return outcomeEvent;
  }
}
