package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.OutcomeServiceImpl;

import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private OutcomeServiceImpl cometTranslationService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  public ResponseEntity<HouseholdOutcome> householdCaseOutcomeResponse(
      HouseholdOutcome householdOutcome) throws GatewayException {
    gatewayEventManager
        .triggerEvent(String.valueOf(householdOutcome.getCaseId()), COMET_OUTCOME_RECEIVED, LocalTime.now());
    cometTranslationService.createHouseHoldOutcomeEvent(householdOutcome);

    return new ResponseEntity<>(householdOutcome, HttpStatus.ACCEPTED);
  }
}
