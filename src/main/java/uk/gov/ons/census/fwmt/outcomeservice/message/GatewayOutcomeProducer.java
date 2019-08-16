package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;

@Slf4j
@Component
public class GatewayOutcomeProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  private MessageConverter messageConverter;

  @Autowired
  private ObjectMapper om;

  @Retryable
  public void sendAddressUpdate(String outcomeEvent, String transactionId) {
//    log.info("Address Update message sent to queue :{}", transactionId);
//
//    MessageProperties messageProperties = new MessageProperties();
//
//
//    //messageProperties.setContentType("application/json");
//
//    //om.readTree(outcomeEvent);
//    //MessageConverter messageConverter = new Jackson2JsonMessageConverter();
//
//    Message message = messageConverter.toMessage(outcomeEvent, messageProperties);
//
//    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
//        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY, message);
    log.info("Address Update message sent to queue :{}", transactionId);
    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
            GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY, outcomeEvent);
  }

  @Retryable
  public void sendCcsIntQuestionnaire(String outcomeEvent, String transactionId) {
    log.info("CCS linked Questionnaire message sent to queue :{}", transactionId);
    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
            GatewayOutcomeQueueConfig.GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY, outcomeEvent);
  }
  @Retryable
  public void sendRespondentRefusal(String outcomeEvent, String transactionId) {
    log.info("Respondent Refusal message sent to queue :{}", transactionId);
    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
        GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY, outcomeEvent);
  }

  @Retryable
  public void sendFulfilmentRequest(String outcomeEvent, String transactionId) {
    log.info("Fulfillment Request message sent to queue :{}", transactionId);
    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
        GatewayOutcomeQueueConfig.GATEWAY_FULFILLMENT_REQUEST_ROUTING_KEY, outcomeEvent);
  }

  @Retryable
  public void sendPropertyListing(String outcomeEvent, String transactionId) {
    log.info("Property Listing message sent to queue :{}", transactionId);
    rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
        GatewayOutcomeQueueConfig.GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY, outcomeEvent);
  }
}
