package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

@Configuration
public class GatewayEventsConfig {

  public static final String COMET_OUTCOME_RECEIVED = "Comet - Case Outcome Received";
  public static final String OUTCOME_SENT_RM = "Outcome - Case Outcome Sent";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.addEventTypes(new String[] {COMET_OUTCOME_RECEIVED, OUTCOME_SENT_RM});
    return gatewayEventManager;
  }
}
