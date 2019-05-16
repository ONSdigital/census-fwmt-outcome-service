package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

  // Do these need to be static?
  static Map<String, String> householdPaperMap = new HashMap<>();
  static Map<String, String> householdContinuationMap = new HashMap<>();
  static Map<String, String> householdIndividualMap = new HashMap<>();
  static Map<String, String> householdUacMap = new HashMap<>();
  static Map<String, String> individualUacMap = new HashMap<>();

  @Autowired
  public FulfilmentRequestFactory(BuildFulfilmentRequestMaps buildFulfilmentRequestMaps) {
    buildFulfilmentRequestMaps.buildHouseholdPaperRequestMap();
    buildFulfilmentRequestMaps.buildIndividualPaperRequestMap();
    buildFulfilmentRequestMaps.buildHouseholdContinuationPaperRequestMap();
    buildFulfilmentRequestMaps.buildHouseholdUacRequestMap();
    buildFulfilmentRequestMaps.buildIndividualUacRequestMap();
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
      case "Paper H Questionnaire required by post":
      case "Paper HC Questionnaire required by post":
      case "Paper I Questionnaire required by post":
        outcomeEvent = buildFulfilmentPayload(householdOutcome);
        outcomeEventList.add(getQuestionnaireByPost(outcomeEvent, fulfillmentRequest));
        break;
      case "HUAC required by text":
      case "IUAC required by text":
        if (!StringUtils.isEmpty(fulfillmentRequest.getRequesterPhone())) {
          outcomeEvent = buildFulfilmentPayload(householdOutcome);
          outcomeEventList.add(getUacByText(outcomeEvent, fulfillmentRequest));
        } else {
          throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Phone number not provided with Outcome");
        }
        break;
      case "Paper H Questionnaire issued":
      case "Will Complete":
      case "Have Completed":
      case "Collected completed questionnaire":
      case "Call back another time":
      case "Holiday home":
      case "Second residence":
      case "Requested assistance":
        if (!StringUtils.isEmpty(fulfillmentRequest.getQuestionnaireId())) {
          outcomeEvent = buildUacPayload(householdOutcome);
          outcomeEvent.getPayload().getUac().setQuestionnaireId(fulfillmentRequest.getQuestionnaireId());
          outcomeEventList.add(outcomeEvent);
        } else {
          throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Questionnaire ID not provided with Outcome");
        }
        break;
      default:
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
            "Failed to find valid Secondary Outcome " + householdOutcome.getSecondaryOutcome());
      }
    }
    return outcomeEventList.toArray(new OutcomeEvent[0]);
  }

  private OutcomeEvent getQuestionnaireByPost(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest)
      throws GatewayException {

    if (householdPaperMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdPaperMap.get(fulfillmentRequest.getQuestionnaireType()));

    } else if (householdContinuationMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdContinuationMap.get(fulfillmentRequest.getQuestionnaireType()));

    } else if (householdIndividualMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfillment()
          .setProductCode(householdIndividualMap.get(fulfillmentRequest.getQuestionnaireType()));

      if (!StringUtils.isEmpty(fulfillmentRequest.getRequesterTitle())) {
        outcomeEvent.getPayload().getFulfillment().getContact().setTitle(fulfillmentRequest.getRequesterTitle());
      } else {
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Requester Title not provided with Outcome");
      }

      if (!StringUtils.isEmpty(fulfillmentRequest.getRequesterForename())) {
        outcomeEvent.getPayload().getFulfillment().getContact().setForename(fulfillmentRequest.getRequesterForename());
      } else {
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Requester Forename not provided with Outcome");
      }

      if (!StringUtils.isEmpty(fulfillmentRequest.getRequesterSurname())) {
        outcomeEvent.getPayload().getFulfillment().getContact().setSurname(fulfillmentRequest.getRequesterSurname());
      } else {
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Requester Surname not provided with Outcome");
      }

    } else {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "Failed to find valid Questionnaire Type " + fulfillmentRequest.getQuestionnaireType());
    }
    return outcomeEvent;
  }

  private OutcomeEvent getUacByText(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest)
      throws GatewayException {
    if (householdUacMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {
      getRequesterPhone(outcomeEvent, fulfillmentRequest, householdUacMap);
    } else if (individualUacMap.containsKey(fulfillmentRequest.getQuestionnaireType())) {
      getRequesterPhone(outcomeEvent, fulfillmentRequest, individualUacMap);
    } else {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "Failed to find valid Questionnaire Type " + fulfillmentRequest.getQuestionnaireType());
    }
    return outcomeEvent;
  }

  private void getRequesterPhone(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest,
      Map<String, String> householdUacMap) throws GatewayException {
    outcomeEvent.getPayload().getFulfillment()
        .setProductCode(householdUacMap.get(fulfillmentRequest.getQuestionnaireType()));

    if (!StringUtils.isEmpty(fulfillmentRequest.getRequesterPhone())) {
      outcomeEvent.getPayload().getFulfillment().getContact().setTelNo(fulfillmentRequest.getRequesterPhone());
    } else {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Requester Phone not provided with Outcome");
    }
  }
}
