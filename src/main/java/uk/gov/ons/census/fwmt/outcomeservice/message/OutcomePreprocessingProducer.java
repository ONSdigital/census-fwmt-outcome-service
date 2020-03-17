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
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

import java.io.IOException;

@Slf4j
@Component
public class OutcomePreprocessingProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;



  @Retryable
  public void sendOutcomeToPreprocessingQueue(SPGOutcome spgOutcome) {
      rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
          OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, spgOutcome);
  }

  @Retryable
  public void sendNewUnitAddressToPreprocessingQueue(NewUnitAddress newUnitAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newUnitAddress);
  }

  @Retryable
  public void sendNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newStandaloneAddress);
  }
}
