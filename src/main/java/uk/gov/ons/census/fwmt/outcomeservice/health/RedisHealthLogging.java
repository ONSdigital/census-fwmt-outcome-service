package uk.gov.ons.census.fwmt.outcomeservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.REDIS_SERVICE_DOWN;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.REDIS_SERVICE_UP;

@Component
public class RedisHealthLogging extends AbstractHealthIndicator {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private RedisConnectionFactory redisConnectionFactory;

  private
  RedisConnection connection;

  public RedisHealthLogging(@Qualifier("redisConnectionFactory") RedisConnectionFactory connectionFactory) {
    super("Redis health check failed");
    Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
    this.redisConnectionFactory = connectionFactory;
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) {
    try {
      connection = RedisConnectionUtils.getConnection(this.redisConnectionFactory);
      builder.up();
      gatewayEventManager.triggerEvent("<N/A>", REDIS_SERVICE_UP);
      return;
    } catch (Exception e) {
      builder.down().withDetail(e.getMessage(), Exception.class);
    } finally {
      if (connection != null) {
        RedisConnectionUtils.releaseConnection(connection, this.redisConnectionFactory);
      }
    }
    gatewayEventManager.triggerErrorEvent(this.getClass(), "Cannot reach Redis", "<NA>", REDIS_SERVICE_DOWN);
  }
}
