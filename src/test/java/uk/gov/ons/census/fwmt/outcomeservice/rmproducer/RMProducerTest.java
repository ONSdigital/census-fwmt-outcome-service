package uk.gov.ons.census.fwmt.outcomeservice.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.Event;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerTest {

  @InjectMocks
  GatewayOutcomeProducer rmProducer;

  @Mock
  RabbitTemplate template;

  @Mock
  ObjectMapper objectMapper;

  @Test
  public void send() throws JsonProcessingException, GatewayException {
    //Given
    OutcomeEvent outcomeEvent = new OutcomeEvent();
    Event event_ = new Event();
    event_.setTransactionId(UUID.fromString("c45de4dc-3c3b-11e9-b210-d663bd873d93"));
    outcomeEvent.setEvent(event_);

    final String responseJson = "  {\n"
        + "  \"Event\" : {\n"
        + "    \"type\" : \"AddressNotValid\",\n"
        + "    \"source\" : \"FieldworkGateway\",\n"
        + "    \"channel\" : \"field\",\n"
        + "    \"dateTime\" : \"2011-08-12T20:17:46.384Z\",\n"
        + "    \"transactionId\" : \"c45de4dc-3c3b-11e9-b210-d663bd873d93\"\n"
        + "},\n"
        + "  \"payload\" : {\n"
        + "    \"InvalidAddress\" : {\n"
        + "        \"reason\":\"derelict\",\n"
        + "        \"collectionCase\" : {\n"
        + "          \"id\":\"c45de4dc-3c3b-11e9-b210-d663bd873d93\"\n"
        + "        }\n"
        + "    }\n"
        + "  }\n"
        + "}";

    when(objectMapper.writeValueAsString(eq(outcomeEvent))).thenReturn(responseJson);

    //When
    rmProducer.send(outcomeEvent);

    //Then
    verify(objectMapper).writeValueAsString(eq(outcomeEvent));
    verify(template).convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE, GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_ROUTING_KEY, responseJson);

  }
}