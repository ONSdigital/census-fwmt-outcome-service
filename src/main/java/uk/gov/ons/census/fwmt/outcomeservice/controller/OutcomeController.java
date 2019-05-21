package uk.gov.ons.census.fwmt.outcomeservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "FWMT Census Outcome Service", description = "Operations pertaining to receiving outcomes from COMET")
public class OutcomeController {

  @Autowired
  private OutcomeServiceImpl cometTranslationService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @ApiOperation(value = "Post an outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Outcome successfully received"),
      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PostMapping(value = "/HouseholdOutcome", consumes = "application/json", produces = "application/json")
  public void householdCaseOutcomeResponse(@RequestBody HouseholdOutcome householdOutcome) throws GatewayException {
    gatewayEventManager
        .triggerEvent(String.valueOf(householdOutcome.getCaseId()), COMET_OUTCOME_RECEIVED, LocalTime.now());
    cometTranslationService.createHouseHoldOutcomeEvent(householdOutcome);
  }

  @PostMapping(value = "/CCSOutcome", consumes = "application/json", produces = "application/json")
  public void ccsCaseOutcomeResponse() {
    // ccs logic?
  }
}
