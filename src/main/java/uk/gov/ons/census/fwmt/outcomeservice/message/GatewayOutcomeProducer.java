package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;

import java.io.IOException;

@Slf4j
@Component
public class GatewayOutcomeProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Retryable
  public void sendAddressUpdate(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Address Update message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process address update for transaction ID " + transactionId);
    }
  }

  @Retryable
  public void sendCcsIntQuestionnaire(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("CCS linked Questionnaire message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process CCS interview for transaction ID " + transactionId);
    }
  }

  @Retryable
  public void sendRespondentRefusal(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Respondent Refusal message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process respondent for transaction ID " + transactionId);
    }

  }

  @Retryable
  public void sendFulfilmentRequest(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Fulfilment Request message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process fulfilment request for transaction ID " + transactionId);
    }
  }

  @Retryable
  public void sendQuestionnaireLinked(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Questionnaire Linked message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process questionnaire linked for transaction ID " + transactionId);
    }
  }

  @Retryable
  public void sendPropertyListing(String outcomeEvent, String transactionId) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Property Listing message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeEvent), messageProperties);

      rabbitTemplate.convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
          GatewayOutcomeQueueConfig.GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY, message);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Cannot process CCS property listing for transaction ID " + transactionId);
    }
  }
}
