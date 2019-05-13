package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.FulfilmentRequestMapping;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.FulfillmentRequest;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Contact;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Fulfilment;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Payload;

import java.util.ArrayList;
import java.util.List;

@Component
public class FulfilmentRequestFactory {

  private FulfilmentRequestMapping fulfilmentRequestMapping = new FulfilmentRequestMapping();

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
    event.setTransactionId(householdOutcome.getTransactionId());
    event.setDateTime(householdOutcome.getEventDate());
    outcomeEvent.setEvent(event);

    return getFulfilmentRequest(householdOutcome, outcomeEvent);
  }

  private OutcomeEvent[] getFulfilmentRequest(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent)
      throws GatewayException {
    List<OutcomeEvent> outcomeEventList = new ArrayList<>();
    for (FulfillmentRequest fulfillmentRequest : householdOutcome.getFulfillmentRequests()) {

      outcomeEvent.getPayload().getFulfilment().setCaseId(householdOutcome.getCaseId());

      switch (householdOutcome.getSecondaryOutcome()) {
      case "Paper H Questionnaire required by post":
        outcomeEvent.getPayload().getFulfilment()
            .setProductCode(getHouseholdContinuationProductCode(fulfillmentRequest));

        outcomeEventList.add(outcomeEvent);
        break;
      case "Paper HC Questionnaire required by post":
        outcomeEvent.getPayload().getFulfilment().setProductCode(getPaperRequestedProductCode(fulfillmentRequest));

        outcomeEventList.add(outcomeEvent);
        break;
      case "Paper I Questionnaire by post":
        outcomeEvent.getPayload().getFulfilment().getContact().setTitle("title");
        outcomeEvent.getPayload().getFulfilment().getContact().setForename(fulfillmentRequest.getRequesterName());
        outcomeEvent.getPayload().getFulfilment().getContact().setSurname("surname");
        outcomeEvent.getPayload().getFulfilment()
            .setProductCode(getIndividualPaperRequestProductCode(fulfillmentRequest));

        outcomeEventList.add(outcomeEvent);
        break;
      default:
        throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
            "Failed to process message into JSON." + householdOutcome.getSecondaryOutcome());
      }
    }
    return outcomeEventList.toArray(new OutcomeEvent[0]);
  }

  private String getIndividualPaperRequestProductCode(FulfillmentRequest fulfillmentRequest) {
    String productCode = null;

    if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getIndividualPaperRequestedEnglish())) {
      productCode = fulfilmentRequestMapping.getIndividualPaperRequestedEnglishPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getIndividualPaperRequestedEnglishWelshHeader())) {
      productCode = fulfilmentRequestMapping.getIndividualPaperRequestedEnglishWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getIndividualPaperRequestedWelshWelshHeader())) {
      productCode = fulfilmentRequestMapping.getIndividualPaperRequestedWelshWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getIndividualPaperRequestedEnglishNiHeader())) {
      productCode = fulfilmentRequestMapping.getIndividualPaperRequestedEnglishNiHeaderPackCode();
    }
    return productCode;
  }

  private String getPaperRequestedProductCode(FulfillmentRequest fulfillmentRequest) throws GatewayException {
    String productCode;

    if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getHouseholdPaperRequestedEnglish())) {
      productCode = fulfilmentRequestMapping.getHouseholdPaperRequestedEnglishPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType().equals(
        fulfilmentRequestMapping.getHouseholdPaperRequestedEnglishWelshHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdPaperRequestedEnglishWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType().equals(
        fulfilmentRequestMapping.getHouseholdPaperRequestedWelshWelshHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdPaperRequestedWelshWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType().equals(
        fulfilmentRequestMapping.getHouseholdPaperRequestedEnglishNiHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdPaperRequestedEnglishNiHeaderPackCode();
    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to process message into JSON." + fulfillmentRequest.getQuestionnaireType());
    }
    return productCode;
  }

  private String getHouseholdContinuationProductCode(FulfillmentRequest fulfillmentRequest) throws GatewayException {
    String productCode;
    if (fulfillmentRequest.getQuestionnaireType()
        .equals(fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedEnglishWelshHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedEnglishWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType().equals(
        fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedWelshWelshHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedWelshWelshHeaderPackCode();
    } else if (fulfillmentRequest.getQuestionnaireType().equals(
        fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedEnglishNiHeader())) {
      productCode = fulfilmentRequestMapping.getHouseholdContinuationPaperRequestedEnglishNiHeaderPackCode();
    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Failed to process message into JSON." + fulfillmentRequest.getQuestionnaireType());
    }
    return productCode;
  }
}
