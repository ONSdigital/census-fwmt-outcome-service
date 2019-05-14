package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.FulfilmentRequest;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Contact;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Fulfilment;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;

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

  @Autowired
  private BuildFulfilmentRequestMaps buildFulfilmentRequestMaps;

  public FulfilmentRequestFactory() {
    buildFulfilmentRequestMaps.buildHouseholdPaperRequestMap();
    buildFulfilmentRequestMaps.buildIndividualPaperRequestMap();
    buildFulfilmentRequestMaps.buildHouseholdContinuationPaperRequestMap();
    buildFulfilmentRequestMaps.buildHouseholdUacRequestMap();
    buildFulfilmentRequestMaps.buildIndividualUacRequestMap();
  }

  public OutcomeEvent[] createFulfilmentEvents(HouseholdOutcome householdOutcome) throws GatewayException {
    return newFulfilmentRequestList(householdOutcome);
  }

  private OutcomeEvent[] newFulfilmentRequestList(HouseholdOutcome householdOutcome) throws GatewayException {
    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Payload payload = new Payload();
    Fulfilment fulfilment = new Fulfilment();
    Contact contact = new Contact();

    fulfilment.setContact(contact);
    payload.setFulfilment(fulfilment);

    outcomeEvent.setPayload(payload);

    Event event = new Event();
    event.setSource("FIELDWORK_GATEWAY");
    event.setChannel("FIELD");
    event.setType("FULFILMENT_REQUESTED");
    event.setTransactionId(householdOutcome.getTransactionId());
    event.setDateTime(householdOutcome.getEventDate());
    outcomeEvent.setEvent(event);

    return getFulfilmentRequest(householdOutcome, outcomeEvent);
  }

  private OutcomeEvent[] getFulfilmentRequest(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent)
      throws GatewayException {
    List<OutcomeEvent> outcomeEventList = new ArrayList<>();
    for (FulfilmentRequest fulfilmentRequest : householdOutcome.getFulfilmentRequests()) {
      outcomeEvent.getPayload().getFulfilment().setCaseId(householdOutcome.getCaseId());
      switch (householdOutcome.getSecondaryOutcome()) {
      case "Paper Questionnaire required by post":
        getQuestionnaireByPost(outcomeEvent, outcomeEventList, fulfilmentRequest);
        break;
      case "UAC required by text":
        getUacByText(outcomeEvent, fulfilmentRequest);
        break;
      default:
        throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
            "Failed to find valid Secondary Outcome " + householdOutcome.getSecondaryOutcome());
      }
    }
    return outcomeEventList.toArray(new OutcomeEvent[0]);
  }

  private void getUacByText(OutcomeEvent outcomeEvent, FulfilmentRequest fulfilmentRequest) throws GatewayException {
    if (householdUacMap.containsKey(fulfilmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfilment()
          .setProductCode(householdUacMap.get(fulfilmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfilment().getContact().setTelNo(fulfilmentRequest.getRequesterPhone());

    } else if (individualUacMap.containsKey(fulfilmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfilment()
          .setProductCode(individualUacMap.get(fulfilmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfilment().getContact().setTelNo(fulfilmentRequest.getRequesterPhone());

    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to find valid Questionnaire Type " + fulfilmentRequest.getQuestionnaireType());
    }
  }

  private void getQuestionnaireByPost(OutcomeEvent outcomeEvent, List<OutcomeEvent> outcomeEventList,
      FulfilmentRequest fulfilmentRequest) throws GatewayException {

    if (householdPaperMap.containsKey(fulfilmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfilment()
          .setProductCode(householdPaperMap.get(fulfilmentRequest.getQuestionnaireType()));

      outcomeEventList.add(outcomeEvent);
    } else if (householdContinuationMap.containsKey(fulfilmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfilment()
          .setProductCode(householdContinuationMap.get(fulfilmentRequest.getQuestionnaireType()));

      outcomeEventList.add(outcomeEvent);

    } else if (householdIndividualMap.containsKey(fulfilmentRequest.getQuestionnaireType())) {

      outcomeEvent.getPayload().getFulfilment()
          .setProductCode(householdIndividualMap.get(fulfilmentRequest.getQuestionnaireType()));

      outcomeEvent.getPayload().getFulfilment().getContact().setTitle(fulfilmentRequest.getRequesterTitle());
      outcomeEvent.getPayload().getFulfilment().getContact().setForename(fulfilmentRequest.getRequesterForename());
      outcomeEvent.getPayload().getFulfilment().getContact().setSurname(fulfilmentRequest.getRequesterSurname());

      outcomeEventList.add(outcomeEvent);

    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to find valid Questionnaire Type " + fulfilmentRequest.getQuestionnaireType());
    }
  }
}
