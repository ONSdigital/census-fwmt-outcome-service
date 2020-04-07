package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;

import java.util.UUID;

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
        spgOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", spgOutcome.getOutcomeCode());
    spgOutcome.setCaseId(UUID.fromString(caseId));

    outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewUnitAddress(NewUnitAddress newUnitAddress) {
    UUID newCaseId = UUID.randomUUID();
    gatewayEventManager.triggerEvent(String.valueOf(newCaseId), COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED, "transactionId",
        newUnitAddress.getTransactionId().toString(), "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        newUnitAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newUnitAddress.getOutcomeCode());

    outcomePreprocessingProducer.sendNewUnitAddressToPreprocessingQueue(newUnitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewStandalone(NewStandaloneAddress newStandaloneAddress) {
    UUID newCaseId = UUID.randomUUID();
    gatewayEventManager.triggerEvent(String.valueOf(newCaseId), COMET_CESPGSTANDALONE_OUTCOME_RECEIVED, "transactionId",
        newStandaloneAddress.getTransactionId().toString(), "Primary Outcome",
        newStandaloneAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        newStandaloneAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newStandaloneAddress.getOutcomeCode());

    outcomePreprocessingProducer.sendNewStandaloneAddress(newStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
