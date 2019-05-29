package uk.gov.ons.census.fwmt.outcomeservice.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.comet.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.Contact;
import uk.gov.ons.census.fwmt.common.data.rm.Event;
import uk.gov.ons.census.fwmt.common.data.rm.Fulfillment;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.data.rm.Payload;
import uk.gov.ons.census.fwmt.common.data.rm.Uac;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uk.gov.ons.ctp.integration.common.product.model.Product.CaseType.HI;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Component
@Slf4j
public class FulfilmentRequestFactory {

  @Autowired
  private ProductReference productReference;

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

  private OutcomeEvent[] getFulfilmentRequest(HouseholdOutcome householdOutcome) throws GatewayException {
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
      case "UAC required by text":
        outcomeEvent = buildFulfilmentPayload(householdOutcome);
        outcomeEventList.add(getUacByText(outcomeEvent, fulfillmentRequest));
        break;
      case "Paper Questionnaire issued":
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
        log.error("Failed to find valid Secondary Outcome: ", householdOutcome.getSecondaryOutcome());
        break;
      }
    }
    return outcomeEventList.toArray(new OutcomeEvent[0]);
  }

  private OutcomeEvent getQuestionnaireByPost(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest)
      throws GatewayException {

    Product product = getPackCodeFromQuestionnaireType(fulfillmentRequest);

    if (product.getCaseType().equals(HI)) {
      outcomeEvent.getPayload().getFulfillment().getContact().setTitle(fulfillmentRequest.getRequesterTitle());
      outcomeEvent.getPayload().getFulfillment().getContact().setForename(fulfillmentRequest.getRequesterForename());
      outcomeEvent.getPayload().getFulfillment().getContact().setSurname(fulfillmentRequest.getRequesterSurname());

      outcomeEvent.getPayload().getFulfillment().setProductCode(product.getFieldQuestionnaireCode());
    } else {
      outcomeEvent.getPayload().getFulfillment().setProductCode(product.getFieldQuestionnaireCode());
    }

    return outcomeEvent;
  }

  private OutcomeEvent getUacByText(OutcomeEvent outcomeEvent, FulfillmentRequest fulfillmentRequest)
      throws GatewayException {
    Product product = getPackCodeFromQuestionnaireType(fulfillmentRequest);

    outcomeEvent.getPayload().getFulfillment()
        .setProductCode(product.getFieldQuestionnaireCode());

    outcomeEvent.getPayload().getFulfillment().getContact().setTelNo(fulfillmentRequest.getRequesterPhone());

    return outcomeEvent;
  }

  private Product getPackCodeFromQuestionnaireType(FulfillmentRequest fulfillmentRequest) throws GatewayException {
    Product product = new Product();
    List<Product.RequestChannel> requestChannels = Collections.singletonList(FIELD);

    product.setRequestChannels(requestChannels);
    product.setFieldQuestionnaireCode(fulfillmentRequest.getQuestionnaireType());

    List<Product> productList;
    try {
      productList = productReference.searchProducts(product);
    } catch (CTPException e) {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "CTPException: Unable to find product: " + e);
    }
    return productList.get(0);
  }

}
