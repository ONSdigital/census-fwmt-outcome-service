package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayOutcomeQueueConfig {
  public static final String GATEWAY_OUTCOME_QUEUE = "Gateway.Outcome";
  public static final String GATEWAY_OUTCOME_EXCHANGE = "Gateway.Outcome.Exchange";
  public static final String GATEWAY_OUTCOME_ROUTING_KEY = "Gateway.Outcome.Request";

  public static final String GATEWAY_NON_VALID_HOUSEHOLD_ROUTING_KEY = "replace.key.*";
  public static final String GATEWAY_TEST_HOUSEHOLD_ROUTING_KEY = "other.key.*";

  public static final String GATEWAY_OUTCOME_TEST = "Gateway.Test";

  // Queue
  @Bean
  public Queue gatewayOutcomeQueue() {
    return QueueBuilder.durable(GATEWAY_OUTCOME_QUEUE).build();
  }

  @Bean Queue otherQueue() {
    return QueueBuilder.durable(GATEWAY_OUTCOME_TEST).build();
  }

  //Exchange
  @Bean
  public TopicExchange gatewayOutcomeExchange() {
    return new TopicExchange(GATEWAY_OUTCOME_EXCHANGE);
  }

  // Bindings
  @Bean
  public Binding nonValidHouseholdQueueBinding(@Qualifier("gatewayOutcomeQueue") Queue gatewayOutcomeQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(gatewayOutcomeQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_NON_VALID_HOUSEHOLD_ROUTING_KEY);
  }

  @Bean
  public Binding otherBinding(@Qualifier("otherQueue") Queue otherQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(otherQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_TEST_HOUSEHOLD_ROUTING_KEY);
  }

  //Message Listener
  @Bean
  public SimpleMessageListenerContainer gatewayMessageListener(
      ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }
}
