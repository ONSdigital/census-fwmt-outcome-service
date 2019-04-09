package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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

  // Queue
  @Bean
  public Queue gatewayOutcomeQueue() {
    return QueueBuilder.durable(GATEWAY_OUTCOME_QUEUE).build();
  }

  //Exchange
  @Bean
  public DirectExchange gatewayOutcomeExchange() {
    return new DirectExchange(GATEWAY_OUTCOME_EXCHANGE);
  }

  // Bindings
  @Bean
  public Binding gatewayOutcomeBinding(@Qualifier("gatewayOutcomeQueue") Queue gatewayOutcomeQueue,
      @Qualifier("gatewayOutcomeExchange") DirectExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(gatewayOutcomeQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_OUTCOME_ROUTING_KEY);
  }

  //Message Listener
  @Bean
  public SimpleMessageListenerContainer gatewayMessagerListener(
      ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }
}
