package uk.gov.ons.fwmt.census.feedbackservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;

@Configuration
public class GatewayEventsConfig {


  public static final String COMET_OUTCOME_RECEIVED = "Comet - Case Outcome";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.addEventTypes(new String[] {COMET_OUTCOME_RECEIVED});
    return gatewayEventManager;
  }
}
