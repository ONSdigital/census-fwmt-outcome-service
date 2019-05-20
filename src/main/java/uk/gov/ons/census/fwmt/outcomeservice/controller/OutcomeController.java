package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.OutcomeServiceImpl;

import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;

@RestController
@RequestMapping("/OutcomeController")
public class OutcomeController {

  @Autowired
  private OutcomeServiceImpl cometTranslationService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @PostMapping(value = "/HouseholdOutcome", consumes = "application/json", produces = "application/json")
  public void householdCaseOutcomeResponse(@RequestBody HouseholdOutcome householdOutcome) throws GatewayException {
    gatewayEventManager
        .triggerEvent(String.valueOf(householdOutcome.getCaseId()), COMET_OUTCOME_RECEIVED, LocalTime.now());
    cometTranslationService.createHouseHoldOutcomeEvent(householdOutcome);
  }

  @PostMapping(value = "/ccsOutcome", consumes = "application/json", produces = "application/json")
  public void ccsCaseOutcomeResponse(@RequestBody HouseholdOutcome householdOutcome) throws GatewayException {
    // ccs logic?
  }
}
