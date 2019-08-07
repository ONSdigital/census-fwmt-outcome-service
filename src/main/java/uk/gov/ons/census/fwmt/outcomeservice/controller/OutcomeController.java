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
import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLStore;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.time.LocalTime;

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
  private ObjectMapper objectMapper;

  @Override
  public ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(String caseId,
      HouseholdOutcome householdOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_HH_OUTCOME_RECEIVED, LocalTime.now());
    outcomeService.createHouseHoldOutcomeEvent(householdOutcome);

    return new ResponseEntity<>(householdOutcome, HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<CCSPropertyListingOutcome> ccsPropertyListingCaseOutcomeResponse(
      CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException, JsonProcessingException {

    String stringOutcome = objectMapper.writeValueAsString(ccsPropertyListingOutcome);

    ccsplStore.cacheJob(String.valueOf(ccsPropertyListingOutcome.getPropertyListingCaseId()), stringOutcome);

    gatewayEventManager
        .triggerEvent(String.valueOf(ccsPropertyListingOutcome.getPropertyListingCaseId()),
            COMET_CCSPL_OUTCOME_RECEIVED,
            LocalTime.now());
    outcomeService.createPropertyListingOutcomeEvent(ccsPropertyListingOutcome);

    return new ResponseEntity<>(ccsPropertyListingOutcome, HttpStatus.ACCEPTED);
  }

  @Override public ResponseEntity<CCSInterviewOutcome> ccsInterviewOutcome(String caseId,
      CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_CCSSI_OUTCOME_RECEIVED, LocalTime.now());
    outcomeService.createInterviewOutcomeEvent(ccsInterviewOutcome);
    return null;
  }
}
