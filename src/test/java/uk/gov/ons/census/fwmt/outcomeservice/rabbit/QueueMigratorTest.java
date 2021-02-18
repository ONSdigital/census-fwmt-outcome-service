package uk.gov.ons.census.fwmt.outcomeservice.rabbit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QueueMigratorTest {

  private final static String originQ = "GW.Transient.ErrorQ";
  private final static String destRoutingKey = "";
  private final static String exchange = "theexchange";

  @InjectMocks
  private QueueMigrator queueMigrator;

  @Mock
  private AmqpAdmin gatewayAmqpAdmin;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @Test
  public void shouldNotTryMigrateItemsIfOriginQueueIsEmpty() throws GatewayException {
    Properties queueProps = new Properties();
    when(gatewayAmqpAdmin.getQueueProperties(originQ)).thenReturn(queueProps);
    Message dummyMessage = RabbitTestUtils.createMessage("Response", null);

    queueMigrator.migrate(originQ, destRoutingKey, exchange);
    verify(rabbitTemplate, never()).receive(eq(originQ));
    verify(rabbitTemplate, never()).send(eq(exchange), eq(destRoutingKey), eq(dummyMessage));
  }

  @Test
  public void shouldThrowExceptionIfQueuePropsDontExist() {
    when(gatewayAmqpAdmin.getQueueProperties(originQ)).thenReturn(null);
    Message dummyMessage = RabbitTestUtils.createMessage("Response", null);

    assertThrows(GatewayException.class, () -> {
      queueMigrator.migrate(originQ, destRoutingKey, exchange);
    });
    verify(rabbitTemplate, never()).receive(eq(originQ));
    verify(rabbitTemplate, never()).send(eq(exchange),eq(destRoutingKey), eq(dummyMessage));
  }

  @DisplayName("A Migration amount of only 5 items should should occur")
  @Test
  public void shouldOnlyMigrateNumberOfItemsAtGivenTime() throws GatewayException {
    Message dummyMessage = RabbitTestUtils.createMessage("Response", null);
    when(rabbitTemplate.receive(eq(originQ))).thenReturn(dummyMessage);
    Properties queueProps = new Properties();
    queueProps.setProperty("QUEUE_MESSAGE_COUNT", "5");
    when(gatewayAmqpAdmin.getQueueProperties(originQ)).thenReturn(queueProps);
    queueMigrator.migrate(originQ, destRoutingKey, exchange);
    verify(rabbitTemplate, times(5)).receive(eq(originQ));
    verify(rabbitTemplate, times(5)).send(eq(exchange),eq(destRoutingKey), any(Message.class));
  }
}