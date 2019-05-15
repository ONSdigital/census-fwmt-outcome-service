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

  // Queue names
  public static final String GATEWAY_ADDRESS_UPDATE_QUEUE = "Gateway.Address.Update";
  public static final String GATEWAY_RESPONDENT_REFUSAL_QUEUE = "Gateway.Respondent.Refusal";

  // Exchange name
  public static final String GATEWAY_OUTCOME_EXCHANGE = "Gateway.OutcomeEvent.Exchange";

  // Routing keys
  public static final String GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY = "event.fulfillment.request";
  public static final String GATEWAY_FULFILMENT_CONFIRMED_ROUTING_KEY = "event.fulfillment.confirmed";
  public static final String GATEWAY_RESPONSE_AUTHENTICATED_ROUTING_KEY = "event.response.authenticated";
  public static final String GATEWAY_RESPONSE_RECEIPT_ROUTING_KEY = "event.response.receipt";
  public static final String GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY = "event.respondent.refusal";
  public static final String GATEWAY_UAC_UPDATED_ROUTING_KEY = "event.uac.update";
  public static final String GATEWAY_QUESTIONNAIRE_UPDATED_ROUTING_KEY = "event.questionnaire.update";
  public static final String GATEWAY_CASE_UPDATE_ROUTING_KEY = "event.case.update";
  public static final String GATEWAY_ADDRESS_UPDATE_ROUTING_KEY = "event.case.address.update";
  public static final String GATEWAY_CASE_APPOINTMENT_ROUTING_KEY = "event.case.appointment";
  public static final String GATEWAY_SAMPLEUNIT_UPDATE_ROUTING_KEY = "event.sampleunit.update";

  // Queues
  @Bean
  public Queue addressUpdateQueue() {
    return QueueBuilder.durable(GATEWAY_ADDRESS_UPDATE_QUEUE).build();
  }

  @Bean
  public Queue respondentRefusalQueue() {
    return QueueBuilder.durable(GATEWAY_RESPONDENT_REFUSAL_QUEUE).build();
  }

  //Exchange
  @Bean
  public TopicExchange gatewayOutcomeExchange() {
    return new TopicExchange(GATEWAY_OUTCOME_EXCHANGE);
  }

  // Bindings
  @Bean
  public Binding addressUpdateBinding(@Qualifier("addressUpdateQueue") Queue gatewayOutcomeQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(gatewayOutcomeQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
  }

  @Bean
  public Binding respondentRefusalBinding(@Qualifier("respondentRefusalQueue") Queue otherQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(otherQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);
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
