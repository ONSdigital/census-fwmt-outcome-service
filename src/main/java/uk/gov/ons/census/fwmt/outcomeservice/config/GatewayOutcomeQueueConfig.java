package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayOutcomeQueueConfig {

  // Exchange name
  public static final String GATEWAY_OUTCOME_EXCHANGE = "events";

  // Routing keys
  public static final String GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY = "event.respondent.refusal";
  public static final String GATEWAY_ADDRESS_UPDATE_ROUTING_KEY = "event.case.address.update";
  public static final String GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY = "event.fulfilment.request";
  public static final String GATEWAY_QUESTIONNAIRE_UPDATE_ROUTING_KEY = "event.questionnaire.update";
  public static final String GATEWAY_FIELD_CASE_UPDATE_ROUTING_KEY = "event.fieldcase.update";
  public static final String GATEWAY_CCS_PROPERTY_LISTING_ROUTING_KEY = "event.ccs.propertylisting";
}