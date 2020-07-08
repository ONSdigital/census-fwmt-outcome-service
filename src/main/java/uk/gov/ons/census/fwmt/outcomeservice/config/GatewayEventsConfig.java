package uk.gov.ons.census.fwmt.outcomeservice.config;

import com.godaddy.logging.LoggingConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.Application;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

@Configuration
public class GatewayEventsConfig {

  public static final String COMET_HH_OUTCOME_RECEIVED = "COMET_HH_OUTCOME_RECEIVED";
  public static final String COMET_CCSSI_OUTCOME_RECEIVED = "COMET_CCSSI_OUTCOME_RECEIVED";
  public static final String COMET_CCSPL_OUTCOME_RECEIVED = "COMET_CCSPL_OUTCOME_RECEIVED";
  public static final String COMET_CESPG_OUTCOME_RECEIVED = "COMET_CESPG_OUTCOME_RECEIVED";
  public static final String COMET_CE_OUTCOME_RECEIVED = "COMET_CE_OUTCOME_RECEIVED";
  public static final String COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED = "COMET_CESPG_UNITADDRESS_OUTCOME_RECEIVED";
  public static final String COMET_CESPGSTANDALONE_OUTCOME_RECEIVED = "COMET_CESPG_STANDALONE_OUTCOME_RECEIVED";
  public static final String PREPROCESSING_CESPG_OUTCOME = "PREPROCESSING_CESPG_OUTCOME";
  public static final String PREPROCESSING_CESPGUNITADDRESS_OUTCOME = "PREPROCESSING_CESPGUNITADDRESS_OUTCOME";
  public static final String PREPROCESSING_CESPGSTANDALONE_OUTCOME = "PREPROCESSING_CESPGSTANDALONE_OUTCOME";
  public static final String PROCESSING_CESPG_OUTCOME = "PROCESSING_CESPG_OUTCOME";
  public static final String PROCESSING_CE_OUTCOME = "PROCESSING_CE_OUTCOME";

  //PROPERTY_LISTING_SENT
  public static final String CCSPL_OUTCOME_SENT = "CCSPL_OUTCOME_SENT";
  public static final String CCSPL_CACHED_OUTCOME = "CCSPL_CACHED_OUTCOME";
  public static final String CCSPL_CACHED_FAILED = "CCSPL_CACHED_FAILED";

  //OUTCOMES
  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";
  public static final String OUTCOME_SENT = "OUTCOME_SENT";
  public static final String RM_FIELD_REPUBLISH = "RM_FIELD_REPUBLISH";
  public static final String ADDRESS_NOT_VALID_OUTCOME_SENT = "ADDRESS_NOT_VALID_OUTCOME_SENT";
  public static final String ADDRESS_TYPE_CHANGED_OUTCOME_SENT = "ADDRESS_TYPE_CHANGED_OUTCOME_SENT";
  public static final String FULFILMENT_REQUESTED_OUTCOME_SENT = "FULFILMENT_REQUESTED_OUTCOME_SENT";
  public static final String EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT = "EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT";
  public static final String HARD_REFUSAL_RECEIVED_OUTCOME_SENT = "HARD_REFUSAL_RECEIVED_OUTCOME_SENT";
  public static final String QUESTIONNAIRE_LINKED_OUTCOME_SENT = "QUESTIONNAIRE_LINKED_OUTCOME_SENT";
  public static final String NEW_ADDRESS_REPORTED_OUTCOME_SENT = "NEW_ADDRESS_REPORTED_OUTCOME_SENT";
  public static final String NEW_UNIT_ADDRESS_OUTCOME_SENT = "NEW_UNIT_ADDRESS_OUTCOME_SENT";
  public static final String UPDATE_RESIDENT_COUNT_OUTCOME_SENT = "UPDATE_RESIDENT_COUNT_OUTCOME_SENT";
  public static final String RECEIVED_NO_ACTION_FROM_TM = "RECEIVED_NO_ACTION_FROM_TM";
  public static final String FAILED_TO_LOOKUP_OUTCOME_CODE = "FAILED_TO_LOOKUP_OUTCOME_CODE";

  //OUTCOME_SENT_RM
  public static final String HH_OUTCOME_SENT = "HH_OUTCOME_SENT";
  public static final String CCSI_OUTCOME_SENT = "CCSI_OUTCOME_SENT";
  public static final String FAILED_JSON_CONVERSION = "FAILED_JSON_CONVERSION";
  public static final String FAILED_FULFILMENT_REQUEST_IS_NULL = "FAILED_FULFILMENT_REQUEST_IS_NULL";
  public static final String CCS_FAILED_FULFILMENT_REQUEST_INVALID = "FAILED_FULFILMENT_REQUEST_SIZE_INVALID";
  public static final String FAILED_FULFILMENT_REQUEST_ADDITIONAL_QID_IN_PROPERTY_LISTING = "FAILED_FULFILMENT_REQUEST_ADDITIONAL_QID_IN_PROPERTY_LISTING";

  //Health logging
  public static final String RABBIT_QUEUE_UP = "RABBIT_QUEUE_UP";
  public static final String RABBIT_QUEUE_DOWN = "RABBIT_QUEUE_DOWN";
  public static final String REDIS_SERVICE_UP = "REDIS_SERVICE_UP";
  public static final String REDIS_SERVICE_DOWN = "REDIS_SERVICE_DOWN";
  @Value("#{'${logging.profile}' == 'CLOUD'}")
  private boolean useJsonLogging;

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.setSource(Application.APPLICATION_NAME);
    gatewayEventManager.addEventTypes(new String[] {COMET_HH_OUTCOME_RECEIVED, COMET_CCSSI_OUTCOME_RECEIVED,
        COMET_CCSPL_OUTCOME_RECEIVED, CCSPL_OUTCOME_SENT, HH_OUTCOME_SENT, CCSI_OUTCOME_SENT, RABBIT_QUEUE_UP,
        REDIS_SERVICE_UP, CCSPL_CACHED_OUTCOME, COMET_CESPG_OUTCOME_RECEIVED, COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED,
        COMET_CESPGSTANDALONE_OUTCOME_RECEIVED, OUTCOME_SENT, NEW_UNIT_ADDRESS_OUTCOME_SENT,
        NEW_ADDRESS_REPORTED_OUTCOME_SENT, QUESTIONNAIRE_LINKED_OUTCOME_SENT,
        HARD_REFUSAL_RECEIVED_OUTCOME_SENT, EXTRAORDINARY_REFUSAL_RECEIVED_OUTCOME_SENT,
        FULFILMENT_REQUESTED_OUTCOME_SENT, ADDRESS_TYPE_CHANGED_OUTCOME_SENT,
        ADDRESS_NOT_VALID_OUTCOME_SENT, PROCESSING_CESPG_OUTCOME, PREPROCESSING_CESPGSTANDALONE_OUTCOME,
        PREPROCESSING_CESPGUNITADDRESS_OUTCOME, PREPROCESSING_CESPG_OUTCOME, PROCESSING_CE_OUTCOME,
        COMET_CE_OUTCOME_RECEIVED, UPDATE_RESIDENT_COUNT_OUTCOME_SENT, PROCESSING_OUTCOME, RM_FIELD_REPUBLISH
    });
    gatewayEventManager.addErrorEventTypes(new String[] {FAILED_JSON_CONVERSION, FAILED_FULFILMENT_REQUEST_IS_NULL,
        CCS_FAILED_FULFILMENT_REQUEST_INVALID, RABBIT_QUEUE_DOWN, REDIS_SERVICE_DOWN,
        FAILED_FULFILMENT_REQUEST_ADDITIONAL_QID_IN_PROPERTY_LISTING, RECEIVED_NO_ACTION_FROM_TM,
        FAILED_TO_LOOKUP_OUTCOME_CODE, CCSPL_CACHED_FAILED});

    return gatewayEventManager;
  }

  /**
   * This method needs to be called before the Gateway event Manager is used,
   * if not the logger will not be properly initialised
   */
  @PostConstruct
  public void initJsonLogging() {
    HashMap<Class<?>, Function<Object, String>> customMappers = new HashMap<>();
    customMappers.put(LocalTime.class, Object::toString);
    customMappers.put(LocalDateTime.class, Object::toString);

    LoggingConfigs configs;

    if (useJsonLogging) {
      configs = LoggingConfigs.builder().customMapper(customMappers).build().useJson();
    } else {
      configs = LoggingConfigs.builder().customMapper(customMappers).build();
    }
    LoggingConfigs.setCurrent(configs);
  }
}
