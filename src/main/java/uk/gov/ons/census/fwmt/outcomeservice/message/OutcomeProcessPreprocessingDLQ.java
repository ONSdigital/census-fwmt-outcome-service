package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;

import java.io.IOException;

@Slf4j
@Component
public class OutcomeProcessPreprocessingDLQ {

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @RabbitListener(id = "rabbitDLQ", queues = "Outcome.PreprocessingDLQ", autoStartup = "false")
  public void receiveMessage(Message message) throws GatewayException {
    log.info("Received a message in Outcome queue");
    processStoredMessage(message);
  }

  private void processStoredMessage(Message actualMessage) throws GatewayException {
    JsonNode actualMessageRootNode;
    String processedMessage;
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    processedMessage = messageConverter.fromMessage(actualMessage).toString();

    try {
      actualMessageRootNode = jsonObjectMapper.readTree(processedMessage);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }

    JsonNode caseId = actualMessageRootNode.path("caseId");

    rabbitTemplate.convertAndSend(OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_EXCHANGE,
            OutcomePreprocessingQueueConfig.OUTCOME_PREPROCESSING_ROUTING_KEY, actualMessage);

    log.info("Moving " + caseId.asText() + " job from DLQ to preprocessing queue");
  }

}
