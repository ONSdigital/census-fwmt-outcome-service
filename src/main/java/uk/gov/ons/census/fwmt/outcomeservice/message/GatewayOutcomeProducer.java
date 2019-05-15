package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;

@Slf4j
@Component
public class GatewayOutcomeProducer {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Retryable
  public void sendAddressUpdate(OutcomeEvent outcomeEvent) throws GatewayException {
    try {
      final String notification = objectMapper.writeValueAsString(outcomeEvent);
      log.info("Address Update message sent to queue :{}", outcomeEvent.getEvent().getTransactionId());
      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY, notification);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to process message into JSON.", e);
    }
  }

  @Retryable
  public void sendRespondentRefusal(OutcomeEvent outcomeEvent) throws GatewayException {
    try {
      final String notification = objectMapper.writeValueAsString(outcomeEvent);
      log.info("Respondent Refusal message sent to queue :{}", outcomeEvent.getEvent().getTransactionId());
      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY, notification);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to process message into JSON.", e);
    }
  }

  @Retryable
  public void sendFulfilmentRequest(OutcomeEvent outcomeEvent) throws GatewayException {
    try {
      final String notification = objectMapper.writeValueAsString(outcomeEvent);
      log.info("Fulfillment Request message sent to queue :{}", outcomeEvent.getEvent().getTransactionId());
      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY, notification);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to process message into JSON.", e);
    }
  }
}
