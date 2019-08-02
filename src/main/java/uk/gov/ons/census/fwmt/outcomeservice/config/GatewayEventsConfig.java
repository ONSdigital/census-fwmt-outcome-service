package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

@Configuration
public class GatewayEventsConfig {

  public static final String COMET_HH_OUTCOME_RECEIVED = "Comet - HH Case Outcome Received";
  public static final String COMET_CCSSI_OUTCOME_RECEIVED = "Comet - CCSSI Case Outcome Received";
  public static final String COMET_CCSPL_OUTCOME_RECEIVED = "Comet - CCSPL Case Outcome Received";
  public static final String OUTCOME_SENT_RM = "Outcome - Case Outcome Sent";
  public static final String PROPERTY_LISTING_SENT = "Outcome - Property Listing sent";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.addEventTypes(new String[] {COMET_HH_OUTCOME_RECEIVED, COMET_CCSSI_OUTCOME_RECEIVED,
        COMET_CCSPL_OUTCOME_RECEIVED, OUTCOME_SENT_RM});
    return gatewayEventManager;
  }
}
