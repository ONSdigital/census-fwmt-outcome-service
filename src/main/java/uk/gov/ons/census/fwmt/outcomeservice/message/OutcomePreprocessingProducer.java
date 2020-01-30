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
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

import java.io.IOException;

@Slf4j
@Component
public class OutcomePreprocessingProducer {

  @Autowired
  @Qualifier("fwmtRabbitTemplate")
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Retryable
  public void sendOutcomeToPreprocessingQueue(String outcomeMessage, String caseId, String outcomeType) throws GatewayException {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    messageProperties.setHeader("__OutcomeType__", outcomeType);
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Outcome message sent to pre-processing queue :{}", caseId);
    try{
      Message message = messageConverter.toMessage(objectMapper.readTree(outcomeMessage), messageProperties);

      rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
              OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, message);

    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process address update for transaction ID " + caseId);
    }
  }
}
