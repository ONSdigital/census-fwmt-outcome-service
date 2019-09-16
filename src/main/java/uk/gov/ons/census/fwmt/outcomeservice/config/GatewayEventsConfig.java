package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.Application;

@Configuration
public class GatewayEventsConfig {

  public static final String COMET_HH_OUTCOME_RECEIVED = "COMET_HH_OUTCOME_RECEIVED";
  public static final String COMET_CCSSI_OUTCOME_RECEIVED = "COMET_CCSSI_OUTCOME_RECEIVED";
  public static final String COMET_CCSPL_OUTCOME_RECEIVED = "COMET_CCSPL_OUTCOME_RECEIVED";

  //PROPERTY_LISTING_SENT
  public static final String CCSPL_OUTCOME_SENT = "CCSPL_OUTCOME_SENT";

  //OUTCOME_SENT_RM
  public static final String HH_OUTCOME_SENT = "HH_OUTCOME_SENT";

  public static final String CCSI_OUTCOME_SENT = "CCSI_OUTCOME_SENT";

  public static final String FAILED_JSON_CONVERSION = "FAILED_JSON_CONVERSION";

  public static final String FAILED_FULFILMENT_REQUEST_IS_NULL = "FAILED_FULFILMENT_REQUEST_IS_NULL";

  
  
  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.setSource(Application.APPLICATION_NAME);
    gatewayEventManager.addEventTypes(new String[] {COMET_HH_OUTCOME_RECEIVED, COMET_CCSSI_OUTCOME_RECEIVED,
        COMET_CCSPL_OUTCOME_RECEIVED, CCSPL_OUTCOME_SENT, HH_OUTCOME_SENT, CCSI_OUTCOME_SENT});
    gatewayEventManager.addErrorEventTypes(new String[] {FAILED_JSON_CONVERSION, FAILED_FULFILMENT_REQUEST_IS_NULL});
    
    return gatewayEventManager;
  }
}
