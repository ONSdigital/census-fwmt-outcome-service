package uk.gov.ons.census.fwmt.outcomeservice.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.util.Properties;

@Slf4j
@Component
public class QueueMigrator {

  private static final String QUEUE_MESSAGE_COUNT = "QUEUE_MESSAGE_COUNT";

  @Autowired
  @Qualifier("GW_EVENT_RT")
  private RabbitTemplate template;

  @Autowired
  @Qualifier("gatewayAmqpAdmin")
  private AmqpAdmin gatewayAmqpAdmin;

  public String migrate(String originQ, String destRoutingKey, String exchange) throws GatewayException {
    final Properties props = gatewayAmqpAdmin.getQueueProperties(originQ);
    if (props != null) {
      final Object cntValue = props.get(QUEUE_MESSAGE_COUNT);
      if (cntValue != null) {
        log.info("Migrating {} items from queue {} and redirecting to route {} on exchange {} ", cntValue, originQ, destRoutingKey, exchange);
        int itemsToProcess = Integer.parseInt(cntValue.toString());
        while (itemsToProcess > 0) {
          final Message message = template.receive(originQ);
          if (message != null) {
            template.send(exchange, destRoutingKey, message);
          }
          itemsToProcess--;
        }

        log.info("Completed {} items from queue {} and redirecting to route {}", cntValue, originQ, destRoutingKey, exchange);
        return String.format("Migration complete - No items : %s  OriginQ: %s , Destination route %s Exchange %s", cntValue.toString(), originQ, destRoutingKey, exchange);
      } else {
        log.info("no items to migrate this time. ");
        return "No items to migrate";
      }
    }
    log.info("Failed attempt to migrate, no queue properties");
    throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot migrate as there are no properties for origin queue");
  }

}
