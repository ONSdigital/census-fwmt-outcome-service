package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.census.fwmt.common.retry.GatewayMessageRecover;

import static uk.gov.ons.census.fwmt.outcomeservice.config.ConnectionFactoryBuilder.createConnectionFactory;

@Configuration
public class QueueConfig {
  private String username;
  private String password;
  private String hostname;
  private int port;
  private String virtualHost;

  public QueueConfig(
          @Value("${rabbitmq.fwmt.username}") String username,
          @Value("${rabbitmq.fwmt.password}") String password,
          @Value("${rabbitmq.fwmt.hostname}") String hostname,
          @Value("${rabbitmq.fwmt.port}") Integer port,
          @Value("${rabbitmq.fwmt.virtualHost}") String virtualHost) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.port = port;
    this.virtualHost = virtualHost;
  }

  // Connection Factory
  @Bean
  @Primary
  public ConnectionFactory connectionFactory() {
    return createConnectionFactory(port, hostname, virtualHost, password, username);
  }

  // Interceptor
  @Bean
  public RetryOperationsInterceptor interceptor(
          @Qualifier("retryTemplate") RetryOperations retryOperations) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new GatewayMessageRecover());
    interceptor.setRetryOperations(retryOperations);
    return interceptor;
  }

  @Bean
  public AmqpAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }
}
