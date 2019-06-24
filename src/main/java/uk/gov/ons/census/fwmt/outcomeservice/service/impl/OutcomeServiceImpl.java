package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.factory.FulfilmentRequestFactory;
import uk.gov.ons.census.fwmt.outcomeservice.factory.OutcomeEventFactory;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomeEventFactory outcomeEventFactory;

  @Autowired
  private FulfilmentRequestFactory fulfilmentRequestFactory;

  @Autowired
  private ObjectMapper objectMapper;

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {

    if (householdOutcome.getFulfillmentRequests() == null) {
      String outcomeEvent = outcomeEventFactory.createOutcomeEvent(householdOutcome);


      try {
        JsonNode rootNode = objectMapper.readTree(outcomeEvent);
        JsonNode eventTypeNode = rootNode.get("Event").get("Type");
        String eventType = eventTypeNode.asText();

        JsonNode transactionIdNode = rootNode.get("transactionId");
        String transactionId = transactionIdNode.asText();

      if (eventType.equals("ADDRESS_NOT_VALID") || eventType.equals("ADDRESS_TYPE_CHANGED")) {
        gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, transactionId);

      } else if (eventType.equals("REFUSAL_RECEIVED")) {
        gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent, transactionId);
      }

      gatewayEventManager
            .triggerEvent(String.valueOf(householdOutcome.getCaseId()),
              OUTCOME_SENT_RM, LocalTime.now());
      } catch (IOException e) {
        e.printStackTrace();
      }

    } else if (!householdOutcome.getFulfillmentRequests().isEmpty()) {

      List<String> processedFulfilmentRequests = fulfilmentRequestFactory.createFulfilmentRequestEvent(householdOutcome);

      for (String outcomeEvent : processedFulfilmentRequests) {
        try {
          JsonNode rootNode = objectMapper.readTree(outcomeEvent);
          JsonNode eventTypeNode = rootNode.get("Event").get("Type");
          String eventType = eventTypeNode.asText();

          JsonNode transactionIdNode = rootNode.get("transactionId");
          String transactionId = transactionIdNode.asText();

        if (eventType.equals("FULFILMENT_REQUESTED"))
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, transactionId);

        if (eventType.equals("QUESTIONNAIRE_LINKED"))
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, transactionId);
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }
  }
}
