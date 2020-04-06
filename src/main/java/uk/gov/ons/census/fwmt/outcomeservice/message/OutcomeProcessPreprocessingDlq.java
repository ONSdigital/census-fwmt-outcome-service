package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

import static uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_DLQ;

@Slf4j
@Component
public class OutcomeProcessPreprocessingDlq {

  @Autowired
  @Qualifier("OS_RT")
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private AmqpAdmin amqpAdmin;

  public void processDLQ() throws GatewayException {
    int messageCount;
    Message message;

    try {
      messageCount = (int) amqpAdmin.getQueueProperties(OUTCOME_PREPROCESSING_DLQ).get("QUEUE_MESSAGE_COUNT");

      for (int i = 0; i < messageCount; i++) {
        message = rabbitTemplate.receive(OUTCOME_PREPROCESSING_DLQ);

        rabbitTemplate.send(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
            OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, message);
      }
    } catch (NullPointerException e) {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "No messages in queue");
    }
  }
}
