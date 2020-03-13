package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGSTANDALONE_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPG_OUTCOME_RECEIVED;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Override
  public ResponseEntity<Void> spgOutcomeResponse(String caseId, SPGOutcome spgOutcome) {
    gatewayEventManager.triggerEvent(caseId, COMET_CESPG_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcome,"");

    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : what should i pass as case id for logging etc?
  @Override
  public ResponseEntity<Void> spgNewUnitAddress(NewUnitAddress spgOutcome) {
    gatewayEventManager.triggerEvent("", COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(),"Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());


    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : should site case id be used for logging etc?
  @Override
  public ResponseEntity<Void> spgNewStandalone(NewStandaloneAddress spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent("", COMET_CESPGSTANDALONE_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());


    return new ResponseEntity<>(HttpStatus.OK);
  }
}
