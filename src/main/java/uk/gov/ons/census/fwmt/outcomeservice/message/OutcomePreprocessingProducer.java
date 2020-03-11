package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

@Slf4j
@Component
public class OutcomePreprocessingProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Retryable
  public void sendOutcomeToPreprocessingQueue(SPGOutcome outcomeMessage, String caseId) {
    log.info("Outcome message sent to pre-processing queue :{}", caseId);
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
            OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, outcomeMessage);
  }
}
