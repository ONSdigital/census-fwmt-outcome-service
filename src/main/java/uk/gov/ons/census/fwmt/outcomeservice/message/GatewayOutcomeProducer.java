package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.io.IOException;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE;

@Slf4j
@Service
public class GatewayOutcomeProducer {

  @Autowired
  @Qualifier("rmRabbitTemplate")
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Retryable
  public void sendOutcome(String payload, String transactionId, String routingKey) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info(routingKey + " : message sent to queue :{}", transactionId);

    try {
      Message message = messageConverter.toMessage(objectMapper.readTree(payload), messageProperties);

      rabbitTemplate.convertAndSend(GATEWAY_OUTCOME_EXCHANGE, routingKey, message);

    } catch (IOException e){
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process " + routingKey +
          " for transaction ID " + transactionId);
    }
  }
}
