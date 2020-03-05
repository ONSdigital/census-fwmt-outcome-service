package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static uk.gov.ons.census.fwmt.outcomeservice.config.ConnectionFactoryBuilder.createConnectionFactory;

@Configuration
public class GatewayOutcomeQueueConfig {

  // Exchange name
  public static final String GATEWAY_OUTCOME_EXCHANGE = "events";

  // Routing keys
  public static final String GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY = "event.respondent.refusal";
  public static final String GATEWAY_ADDRESS_UPDATE_ROUTING_KEY = "event.case.address.update";
  public static final String GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY = "event.fulfilment.request";
  public static final String GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY = "event.questionnaire.update";
  public static final String GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY = "event.ccs.propertylisting";

  private String username;
  private String password;
  private String hostname;
  private int port;
  private String virtualHost;

  public GatewayOutcomeQueueConfig(
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

  @Bean
  @Qualifier("rmRabbitTemplate")
  public RabbitTemplate rmRabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate();
    rabbitTemplate.setConnectionFactory(rmConnectionFactory());
    return rabbitTemplate;
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
