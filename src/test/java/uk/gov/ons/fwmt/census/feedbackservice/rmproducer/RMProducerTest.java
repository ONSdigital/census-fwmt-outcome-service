package uk.gov.ons.fwmt.census.feedbackservice.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.feedbackservice.config.GatewayFeedbackQueueConfig;
import uk.gov.ons.fwmt.census.feedbackservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.feedbackservice.message.GatewayFeedbackProducer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerTest {

  @InjectMocks
  GatewayFeedbackProducer rmProducer;

  @Mock
  RabbitTemplate template;

  @Mock
  ObjectMapper objectMapper;

  @Test
  public void send() throws JsonProcessingException, GatewayException {
    //Given
    CensusCaseOutcomeDTO censusCaseOutcomeDTO = new CensusCaseOutcomeDTO("1234567890", "qwertyuiop", "asdfghjkl",
        "test", "test");
    final String responseJson = "{\n"
        + "  \"caseId\": \"1234567890\",\n"
        + "  \"caseReference\": \"qwertyuiop\",\n"
        + "  \"outcome\": \"asdfghjkl\",\n"
        + "  \"outcomeCategory\": \"test\",\n"
        + "  \"outcomeNote\": \"test\"\n"
        + "}";
    when(objectMapper.writeValueAsString(eq(censusCaseOutcomeDTO))).thenReturn(responseJson);

    //When
    rmProducer.send(censusCaseOutcomeDTO);

    //Then
    verify(objectMapper).writeValueAsString(eq(censusCaseOutcomeDTO));
    verify(template).convertAndSend(GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_EXCHANGE, GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_ROUTING_KEY, responseJson);

  }
}