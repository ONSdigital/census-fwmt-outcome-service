package uk.gov.ons.census.fwmt.outcomeservice.controller;

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
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.time.LocalTime;
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

  @Override
  public ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(String caseId, HouseholdOutcome householdOutcome) throws GatewayException{
    gatewayEventManager.triggerEvent(caseId, COMET_HH_OUTCOME_RECEIVED);
    householdOutcome.setCaseId(UUID.fromString(caseId));
    outcomeService.createHouseHoldOutcomeEvent(householdOutcome);

    return new ResponseEntity<>(householdOutcome, HttpStatus.ACCEPTED);
  }

  @Override
  public ResponseEntity<CCSPropertyListingOutcome> ccsPropertyListingCaseOutcomeResponse(CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), COMET_CCSPL_OUTCOME_RECEIVED);
    outcomeService.createPropertyListingOutcomeEvent(ccsPLOutcome);

    return new ResponseEntity<>(ccsPLOutcome, HttpStatus.ACCEPTED);
  }

  @Override public ResponseEntity<CCSInterviewOutcome> ccsInterviewOutcome(String caseId, CCSInterviewOutcome ccsIOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_CCSSI_OUTCOME_RECEIVED);
    outcomeService.createInterviewOutcomeEvent(ccsIOutcome);

    return null;
  }
}
