package uk.gov.ons.census.fwmt.outcomeservice.controller;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CCSPL_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CCSSI_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_HH_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_JSON_CONVERSION;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;
import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLStore;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private CCSPLStore ccsplStore;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Autowired
  private ObjectMapper objectMapper;

  private String outcomeType = null;

  @Override
  public ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(String caseId, HouseholdOutcome householdOutcome) throws GatewayException{
    gatewayEventManager.triggerEvent(caseId, COMET_HH_OUTCOME_RECEIVED, Map.of("transactionId", householdOutcome.getTransactionId().toString()));
    householdOutcome.setCaseId(UUID.fromString(caseId));

    try {
      String householdOutcomeToQueue = objectMapper.writeValueAsString(householdOutcome);
      outcomeType = "Household";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(householdOutcomeToQueue, caseId, outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move household outcome to pre-processing queue.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              errorMessage + " for case Id: " + caseId);
    }

    return new ResponseEntity<>(householdOutcome, HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<CCSPropertyListingOutcome> ccsPropertyListingCaseOutcomeResponse(
      CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {

    String caseId = ccsPLOutcome.getPropertyListingCaseId().toString();
    gatewayEventManager.triggerEvent(caseId, COMET_CCSPL_OUTCOME_RECEIVED, Map.of("transactionId", ccsPLOutcome.getTransactionId().toString()));

    try {
      String ccsPLOutcomeToQueue = objectMapper.writeValueAsString(ccsPLOutcome);
      outcomeType = "CCSPL";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsPLOutcomeToQueue, caseId, outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move household outcome to pre-processing queue.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              errorMessage + " for case Id: " + caseId);
    }

    try {
      ccsplStore.cacheJob(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), ccsPLOutcome);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Unable to cache CCS PL Outcome for caseId " + caseId);
    }
    return new ResponseEntity<>(ccsPLOutcome, HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<CCSInterviewOutcome> ccsInterviewOutcome(String caseId, CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {

    gatewayEventManager.triggerEvent(caseId, COMET_CCSSI_OUTCOME_RECEIVED, Map.of("transactionId", ccsInterviewOutcome.getTransactionId().toString()));
    
    try {
      String ccsInterviewOutcomeToQueue = objectMapper.writeValueAsString(ccsInterviewOutcome);
      outcomeType = "CCSINT";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsInterviewOutcomeToQueue, caseId, outcomeType);
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move household outcome to pre-processing queue.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), e, errorMessage, caseId, FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              errorMessage + " for case Id: " + caseId);
    }

    return new ResponseEntity<>(ccsInterviewOutcome, HttpStatus.ACCEPTED);
  }
}
