package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayOutcomeQueueConfig {

  // Exchange name
  public static String GATEWAY_OUTCOME_EXCHANGE;

  // Queue names
  public static final String FIELD_REFUSALS_QUEUE = "Field.refusals";
  public static final String TEMP_FIELD_OTHERS_QUEUE = "Field.other";

  GatewayOutcomeQueueConfig(@Value("${rabbitmq.rmOutcomeExchange}") String GATEWAY_OUTCOME_EXCHANGE) {
    GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE = GATEWAY_OUTCOME_EXCHANGE;
  }

  // Routing keys
  // keys mentioned by Dave Mort
  public static final String GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY = "event.respondent.refusal";
  public static final String GATEWAY_ADDRESS_UPDATE_ROUTING_KEY = "event.case.address.update";
  public static final String GATEWAY_FULFILLMENT_REQUEST_ROUTING_KEY = "event.fulfillment.request";
  public static final String GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY = "event.questionnaire.update";
  public static final String GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY = "event.ccs.propertylisting";
  public static final String GATEWAY_EVENT_FIELDCASE_UPDATE_ROUTING_KEY = "event.fieldcase.update";
  public static final String GATEWAY_CASE_APPOINTMENT_ROUTING_KEY = "event.case.appointment";
  public static final String GATEWAY_RESPONSE_AUTHENTICATION_ROUTING_KEY = "event.response.authentication";

  // keys unused but in RM data dictionary
  public static final String GATEWAY_FULFILLMENT_CONFIRMED_ROUTING_KEY = "event.fulfillment.confirmed";
  public static final String GATEWAY_RESPONSE_RECEIPT_ROUTING_KEY = "event.response.receipt";
  public static final String GATEWAY_UAC_UPDATED_ROUTING_KEY = "event.uac.update";
  public static final String GATEWAY_CASE_UPDATE_ROUTING_KEY = "event.case.update";
  public static final String GATEWAY_SAMPLEUNIT_UPDATE_ROUTING_KEY = "event.sampleunit.update";

  // Queues
  @Bean
  public Queue respondentRefusalQueue() {
    return QueueBuilder.durable(FIELD_REFUSALS_QUEUE).build();
  }

  @Bean
  public Queue eventFieldcaseUpdateQueue() {
    return QueueBuilder.durable(TEMP_FIELD_OTHERS_QUEUE).build();
  }

  @Bean
  public Queue addressUpdateQueue() {
    return QueueBuilder.durable(TEMP_FIELD_OTHERS_QUEUE).build();
  }

  @Bean
  public Queue ccsPropertyListing() {
    return QueueBuilder.durable(TEMP_FIELD_OTHERS_QUEUE).build();
  }

  @Bean
  public Queue questionnaireUpdateQueue() {
    return QueueBuilder.durable(TEMP_FIELD_OTHERS_QUEUE).build();
  }

  @Bean
  public Queue fulfillmentRequestQueue() {
    return QueueBuilder.durable(TEMP_FIELD_OTHERS_QUEUE).build();
  }

  //Exchange
  @Bean
  public TopicExchange gatewayOutcomeExchange() {
    return new TopicExchange(GATEWAY_OUTCOME_EXCHANGE);
  }

  // Bindings
  @Bean
  public Binding respondentRefusalBinding(@Qualifier("respondentRefusalQueue") Queue respondentRefusalQueue,
                                          @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(respondentRefusalQueue).to(gatewayOutcomeExchange)
            .with(GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY);
  }

  @Bean
  public Binding addressUpdateBinding(@Qualifier("addressUpdateQueue") Queue addressUpdateQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(addressUpdateQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
  }

  @Bean
  public Binding ccsPropertyListingBinding(@Qualifier("ccsPropertyListing") Queue ccsPropertyListing,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(ccsPropertyListing).to(gatewayOutcomeExchange)
        .with(GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY);
  }

  @Bean
  public Binding fulfillmentRequestBinding(@Qualifier("fulfillmentRequestQueue") Queue fulfillmentRequestQueue,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(fulfillmentRequestQueue).to(gatewayOutcomeExchange)
        .with(GATEWAY_FULFILLMENT_REQUEST_ROUTING_KEY);
  }

  @Bean
  public Binding questionnaireUpdateBinding(@Qualifier("questionnaireUpdateQueue") Queue ccsPropertyListing,
      @Qualifier("gatewayOutcomeExchange") TopicExchange gatewayOutcomeExchange) {
    return BindingBuilder.bind(ccsPropertyListing).to(gatewayOutcomeExchange)
            .with(GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY);
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
