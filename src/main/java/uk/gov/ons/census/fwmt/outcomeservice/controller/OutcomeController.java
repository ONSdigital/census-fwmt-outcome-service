package uk.gov.ons.census.fwmt.outcomeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingReceiver;
import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLStore;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CCSPL_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CCSSI_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_HH_OUTCOME_RECEIVED;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private OutcomeService outcomeService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private CCSPLStore ccsplStore;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Autowired
  private OutcomePreprocessingReceiver outcomePreprocessingReceiver;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(String caseId, HouseholdOutcome householdOutcome) throws GatewayException{

    gatewayEventManager.triggerEvent(caseId, COMET_HH_OUTCOME_RECEIVED);

    householdOutcome.setCaseId(UUID.fromString(caseId));

    try {
      String householdOutcomeToQueue = objectMapper.writeValueAsString(householdOutcome);
      householdOutcomeToQueue = householdOutcomeToQueue + "HouseholdObject";
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(householdOutcomeToQueue, caseId);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              "Unable to move household outcome to pre-processing queue for case ID" + caseId);
    }

//    outcomeService.createHouseHoldOutcomeEvent(householdOutcome);

    return new ResponseEntity<>(householdOutcome, HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<CCSPropertyListingOutcome> ccsPropertyListingCaseOutcomeResponse(
      CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {

    String caseId = ccsPLOutcome.getPropertyListingCaseId().toString();

    try {
      String ccsPLOutcomeToQueue = objectMapper.writeValueAsString(ccsPLOutcome);
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsPLOutcomeToQueue, caseId);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              "Unable to move household outcome to pre-processing queue for case ID" + caseId);
    }

    try {
      ccsplStore.cacheJob(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), ccsPLOutcome);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Unable to cache CCS PL Outcome for caseId " + caseId);
    }

    gatewayEventManager.triggerEvent(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), COMET_CCSPL_OUTCOME_RECEIVED);
    outcomeService.createPropertyListingOutcomeEvent(ccsPLOutcome);

    return new ResponseEntity<>(ccsPLOutcome, HttpStatus.ACCEPTED);
  }

  @Override public ResponseEntity<CCSInterviewOutcome> ccsInterviewOutcome(String caseId, CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {

    try {
      String ccsInterviewOutcomeToQueue = objectMapper.writeValueAsString(ccsInterviewOutcome);
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(ccsInterviewOutcomeToQueue, caseId);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
              "Unable to move household outcome to pre-processing queue for case ID" + caseId);
    }

    gatewayEventManager.triggerEvent(caseId, COMET_CCSSI_OUTCOME_RECEIVED);
    outcomeService.createInterviewOutcomeEvent(ccsInterviewOutcome);

    return null;
  }
}
