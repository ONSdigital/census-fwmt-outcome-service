package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HHNewSplitAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

@Slf4j
@Component
public class OutcomePreprocessingProducer {

  @Autowired
  @Qualifier("OS_RT")
  private RabbitTemplate rabbitTemplate;

  @Retryable
  public void sendSpgOutcomeToPreprocessingQueue(SPGOutcome spgOutcome) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, spgOutcome, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendSpgNewUnitAddressToPreprocessingQueue(SPGNewUnitAddress newUnitAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newUnitAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendSpgNewStandaloneAddress(SPGNewStandaloneAddress newStandaloneAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newStandaloneAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendCeOutcomeToPreprocessingQueue(CEOutcome ceOutcome) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, ceOutcome, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendCeNewUnitAddressToPreprocessingQueue(CENewUnitAddress newUnitAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newUnitAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendCeNewStandaloneAddressToPreprocessingQueue(CENewStandaloneAddress newStandaloneAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, newStandaloneAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendHHOutcomeToPreprocessingQueue(HHOutcome hhOutcome) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, hhOutcome, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendHHSplitAddressToPreprocessingQueue(HHNewSplitAddress hhNewSplitAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, hhNewSplitAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendHHStandaloneAddressToPreprocessingQueue(HHNewStandaloneAddress hhNewStandaloneAddress) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, hhNewStandaloneAddress, new MessagePostProcessor() {
      
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        long epochMilli = Instant.now().toEpochMilli();
        message.getMessageProperties().setTimestamp(new Date(epochMilli));
        return message;
      }
    });
  }

  @Retryable
  public void sendCcsPropertyListingToPreprocessingQueue(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, ccsPropertyListingOutcome);
  }

  @Retryable
  public void sendCcsInterviewToPreprocessingQueue(CCSInterviewOutcome ccsInterviewOutcome) {
    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
        OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, ccsInterviewOutcome);
  }
}
