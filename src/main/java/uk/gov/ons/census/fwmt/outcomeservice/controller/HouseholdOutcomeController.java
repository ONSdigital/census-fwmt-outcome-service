package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.OutcomeServiceImpl;

@RestController
@RequestMapping("/CensusCaseOutcome")
public class HouseholdOutcomeController {

  @Autowired
  private OutcomeServiceImpl cometTranslationService;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void censusCaseOutcomeResponse(@RequestBody HouseholdOutcome householdOutcome) throws GatewayException {
    // translation of comet to RM
    cometTranslationService.CreateHouseHoldOutComeEvent(householdOutcome);
  }
}
