package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OutcomePreprocessingProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Retryable
  public void sendOutcomeToPreprocessingQueue(String outcomeMessage, String caseId, String outcomeType) {
    Map map = new HashMap();
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    log.info("Outcome message sent to pre-processing queue :{}", caseId);

    map.put(outcomeType, outcomeMessage);

    Message message = messageConverter.toMessage(map, messageProperties);

    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
            OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, message);
  }
}
