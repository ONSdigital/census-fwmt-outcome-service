package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.census.fwmt.common.retry.DefaultListenerSupport;
import uk.gov.ons.census.fwmt.common.retry.GatewayMessageRecover;
import uk.gov.ons.census.fwmt.common.retry.GatewayRetryPolicy;

@Configuration
public class RabbitMqConfig {
  private final String username;
  private final String password;
  private final String hostname;
  private final int port;
  private final String virtualHost;

  private final int initialInterval;
  private final double multiplier;
  private final int maxInterval;
  private final int maxRetries;

  private final String rmFieldQueue;

  public RabbitMqConfig(
      @Value("${app.rabbitmq.rm.username}") String username,
      @Value("${app.rabbitmq.rm.password}") String password,
      @Value("${app.rabbitmq.rm.host}") String hostname,
      @Value("${app.rabbitmq.rm.port}") int port,
      @Value("${app.rabbitmq.rm.virtualHost}") String virtualHost,
      @Value("${app.rabbitmq.rm.initialInterval}") int initialInterval,
      @Value("${app.rabbitmq.rm.multiplier}") double multiplier,
      @Value("${app.rabbitmq.rm.maxInterval}") int maxInterval,
      @Value("${app.rabbitmq.rm.maxRetries:1}") int maxRetries,
      @Value("${app.rabbitmq.rm.prefetchCount}") int prefetchCount,
      @Value("${app.rabbitmq.rm.queues.rm.input}") String inputQueue,
      @Value("${app.rabbitmq.rm.queues.rm.dlq}") String inputDlq,
      @Value("${app.rabbitmq.rm.queues.rm.field}") String rmFieldQueue) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.port = port;
    this.virtualHost = virtualHost;
    this.initialInterval = initialInterval;
    this.multiplier = multiplier;
    this.maxInterval = maxInterval;
    this.maxRetries = maxRetries;
    this.rmFieldQueue = rmFieldQueue;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(initialInterval);
    backOffPolicy.setMultiplier(multiplier);
    backOffPolicy.setMaxInterval(maxInterval);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    GatewayRetryPolicy gatewayRetryPolicy = new GatewayRetryPolicy(maxRetries);
    retryTemplate.setRetryPolicy(gatewayRetryPolicy);

    retryTemplate.registerListener(new DefaultListenerSupport());

    return retryTemplate;
  }

  @Bean
  public RetryOperationsInterceptor interceptor(
      @Qualifier("retryTemplate") RetryOperations retryOperations) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new GatewayMessageRecover());
    interceptor.setRetryOperations(retryOperations);
    return interceptor;
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostname, port);

    cachingConnectionFactory.setVirtualHost(virtualHost);
    cachingConnectionFactory.setPassword(password);
    cachingConnectionFactory.setUsername(username);

    return cachingConnectionFactory;
  }
}
