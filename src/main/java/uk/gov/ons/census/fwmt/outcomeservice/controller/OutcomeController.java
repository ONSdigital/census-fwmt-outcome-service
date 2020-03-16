package uk.gov.ons.census.fwmt.outcomeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGSTANDALONE_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.COMET_CESPG_OUTCOME_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_JSON_CONVERSION;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public ResponseEntity<Void> spgOutcomeResponse(String caseId, SPGOutcome spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseId, COMET_CESPG_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());
    spgOutcome.setCaseId(UUID.fromString(caseId));
    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue,
          String.valueOf(spgOutcome.getCaseId()), "SPG_OUTCOME");
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG outcome to pre-processing queue.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, errorMessage, String.valueOf(spgOutcome.getCaseId()),
              FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + spgOutcome.getCaseId());
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : what should i pass as case id for logging etc?
  @Override
  public ResponseEntity<Void> spgNewUnitAddress(NewUnitAddress spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent("", COMET_CESPGUNITADDRESS_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue, "", "NEW_UNIT_ADDRESS");
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG outcome to pre-processing queue.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, errorMessage, "caseId", FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + "caseId");
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : should site case id be used for logging etc?
  @Override
  public ResponseEntity<Void> spgNewStandalone(NewStandaloneAddress spgOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent("", COMET_CESPGSTANDALONE_OUTCOME_RECEIVED, "transactionId",
        spgOutcome.getTransactionId().toString(), "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome",
        spgOutcome.getSecondaryOutcomeDescription());

    try {
      String spgOutcomeToQueue = objectMapper.writeValueAsString(spgOutcome);
      outcomePreprocessingProducer.sendOutcomeToPreprocessingQueue(spgOutcomeToQueue, "", "NEW_STANDALONE_ADDRESS");
    } catch (JsonProcessingException e) {
      String errorMessage = "Unable to move SPG outcome to pre-processing queue.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, errorMessage, "caseId", FAILED_JSON_CONVERSION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          errorMessage + " for case Id: " + "caseId");
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
