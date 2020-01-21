package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static uk.gov.ons.census.fwmt.outcomeservice.config.ConnectionFactoryBuilder.createConnectionFactory;

@Configuration
public class RmQueueConfig {

  private String username;
  private String password;
  private String hostname;
  private int port;
  private String virtualHost;

  public RmQueueConfig(
      @Value("${rabbitmq.rm.username}") String username,
      @Value("${rabbitmq.rm.password}") String password,
      @Value("${rabbitmq.rm.hostname}") String hostname,
      @Value("${rabbitmq.rm.port}") Integer port,
      @Value("${rabbitmq.rm.virtualHost}") String virtualHost) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.port = port;
    this.virtualHost = virtualHost;
  }

  // Connection Factory
  @Bean
  public ConnectionFactory rmConnectionFactory() {
    return createConnectionFactory(port, hostname, virtualHost, password, username);
  }

  // Amqp Admin
  @Bean
  public AmqpAdmin rmAmqpAdmin() {
    return new RabbitAdmin(rmConnectionFactory());
  }
}
