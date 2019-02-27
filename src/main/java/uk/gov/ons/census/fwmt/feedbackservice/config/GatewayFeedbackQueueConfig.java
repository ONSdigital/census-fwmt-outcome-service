package uk.gov.ons.census.fwmt.feedbackservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayFeedbackQueueConfig {
  @Autowired
  private AmqpAdmin amqpAdmin;
    
  public static final String GATEWAY_FEEDBACK_QUEUE = "Gateway.Feedback";
  public static final String GATEWAY_FEEDBACK_EXCHANGE = "Gateway.Feedback.Exchange";
  public static final String GATEWAY_FEEDBACK_ROUTING_KEY = "Gateway.Feedback.Request";

  // Queue
  @Bean
  public Queue gatewayFeedbackQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_FEEDBACK_QUEUE).build();
    return queue;
  }
  
  //Exchange
  @Bean
  public DirectExchange gatewayFeedbackExchange() {
    DirectExchange directExchange = new DirectExchange(GATEWAY_FEEDBACK_EXCHANGE);
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding gatewayFeedbackBinding(@Qualifier("gatewayFeedbackQueue") Queue gatewayFeedbackQueue,
      @Qualifier("gatewayFeedbackExchange") DirectExchange gatewayFeedbackExchange) {
    Binding binding = BindingBuilder.bind(gatewayFeedbackQueue).to(gatewayFeedbackExchange)
        .with(GATEWAY_FEEDBACK_ROUTING_KEY);
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
