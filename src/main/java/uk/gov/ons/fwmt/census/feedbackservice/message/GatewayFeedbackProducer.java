package uk.gov.ons.fwmt.census.feedbackservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.feedbackservice.config.GatewayFeedbackQueueConfig;
import uk.gov.ons.fwmt.census.feedbackservice.data.dto.CensusCaseOutcomeDTO;

@Slf4j
@Component
public class GatewayFeedbackProducer {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RabbitTemplate template;

  @Retryable
  public void send(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    try {
      final String notification = objectMapper.writeValueAsString(censusCaseOutcomeDTO);
      log.info("Message sent to queue :{}", censusCaseOutcomeDTO.getCaseReference());
      template.convertAndSend(GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_EXCHANGE, GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_ROUTING_KEY, notification);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to process message into JSON.", e);
    }
  }
}
