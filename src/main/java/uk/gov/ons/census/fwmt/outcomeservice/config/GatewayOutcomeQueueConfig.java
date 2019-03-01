package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayOutcomeQueueConfig {
  @Autowired
  private AmqpAdmin amqpAdmin;
    
  public static final String GATEWAY_OUTCOME_QUEUE = "Gateway.Outcome";
  public static final String GATEWAY_OUTCOME_EXCHANGE = "Gateway.Outcome.Exchange";
  public static final String GATEWAY_OUTCOME_ROUTING_KEY = "Gateway.Outcome.Request";

  // Queue
  @Bean
  public Queue gatewayOutcomeQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_OUTCOME_QUEUE).build();
    return queue;
  }
  
  //Exchange
  @Bean
  public DirectExchange gatewayOutcomeExchange() {
    DirectExchange directExchange = new DirectExchange(GATEWAY_OUTCOME_EXCHANGE);
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding gatewayOutcomeBinding(@Qualifier("gatewayOutcomeQueue") Queue gatewayOutcomeQueue,
      @Qualifier("gatewayOutcomeExchange") DirectExchange gatewayOutcomeExchange) {
    Binding binding = BindingBuilder.bind(gatewayOutcomeQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_OUTCOME_ROUTING_KEY);
    return binding;
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
