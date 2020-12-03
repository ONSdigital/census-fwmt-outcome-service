package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HHNewSplitAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;

import java.util.UUID;

@RestController
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class OutcomeController implements OutcomeApi {

  public static final String COMET_HH_OUTCOME_RECEIVED = "COMET_HH_OUTCOME_RECEIVED";
  public static final String COMET_HH_SPLITADDRESS_RECEIVED = "COMET_HH_SPLITADDRESS_RECEIVED";
  public static final String COMET_HH_STANDALONE_RECEIVED = "COMET_HH_STANDALONE_RECEIVED";
  public static final String COMET_SPG_OUTCOME_RECEIVED = "COMET_SPG_OUTCOME_RECEIVED";
  public static final String COMET_SPG_UNITADDRESS_OUTCOME_RECEIVED = "COMET_SPG_UNITADDRESS_OUTCOME_RECEIVED";
  public static final String COMET_SPG_STANDALONE_OUTCOME_RECEIVED = "COMET_SPG_STANDALONE_OUTCOME_RECEIVED";
  public static final String COMET_CE_OUTCOME_RECEIVED = "COMET_CE_OUTCOME_RECEIVED";
  public static final String COMET_CE_UNITADDRESS_OUTCOME_RECEIVED = "COMET_CE_UNITADDRESS_OUTCOME_RECEIVED";
  public static final String COMET_CE_STANDALONE_OUTCOME_RECEIVED = "COMET_CE_STANDALONE_OUTCOME_RECEIVED";
  public static final String COMET_CCS_PL_RECEIVED = "COMET_CCS_PL_RECEIVED";
  private static final String COMET_CCS_INT_RECEIVED = "COMET_CCS_INT_RECEIVED";
  private static final String TRANSACTION_ID = "Transaction ID";
  private static final String SURVEY_TYPE = "Survey Type";
  private static final String PRIMARY_OUTCOME = "Primary Outcome";
  private static final String SECONDARY_OUTCOME = "Secondary Outcome";
  private static final String OUTCOME_CODE = "Outcome Code";

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Override
  public ResponseEntity<Void> ceOutcomeResponse(String caseId, CEOutcome ceOutcome) {
    gatewayEventManager.triggerEvent(caseId, COMET_CE_OUTCOME_RECEIVED,
        TRANSACTION_ID, ceOutcome.getTransactionId().toString(),
        SURVEY_TYPE, "CE",
        PRIMARY_OUTCOME, ceOutcome.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, ceOutcome.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, ceOutcome.getOutcomeCode(),
        "CEOutcome", ceOutcome.toString());
    ceOutcome.setCaseId(UUID.fromString(caseId));

    outcomePreprocessingProducer.sendCeOutcomeToPreprocessingQueue(ceOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ceNewUnitAddress(CENewUnitAddress newUnitAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_CE_UNITADDRESS_OUTCOME_RECEIVED,
        TRANSACTION_ID, newUnitAddress.getTransactionId().toString(),
        SURVEY_TYPE, "CE",
        "Site Case ID", String.valueOf(newUnitAddress.getSiteCaseId()),
        PRIMARY_OUTCOME, newUnitAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, newUnitAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, newUnitAddress.getOutcomeCode(),
        "CENewUnitAddress", newUnitAddress.toString());
    
    outcomePreprocessingProducer.sendCeNewUnitAddressToPreprocessingQueue(newUnitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ceNewStandalone(CENewStandaloneAddress newStandaloneAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_CE_STANDALONE_OUTCOME_RECEIVED,
        TRANSACTION_ID, newStandaloneAddress.getTransactionId().toString(),
        SURVEY_TYPE, "CE",
        PRIMARY_OUTCOME, newStandaloneAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, newStandaloneAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, newStandaloneAddress.getOutcomeCode(),
        "CENewStandaloneAddress", newStandaloneAddress.toString());
    
    outcomePreprocessingProducer.sendCeNewStandaloneAddressToPreprocessingQueue(newStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgOutcomeResponse(String caseId, SPGOutcome spgOutcome) {
    gatewayEventManager.triggerEvent(caseId, COMET_SPG_OUTCOME_RECEIVED,
        TRANSACTION_ID, spgOutcome.getTransactionId().toString(),
        SURVEY_TYPE, "SPG",
        PRIMARY_OUTCOME, spgOutcome.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, spgOutcome.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, spgOutcome.getOutcomeCode(),
        "SPGOutcome", spgOutcome.toString());
    
    spgOutcome.setCaseId(UUID.fromString(caseId));
    outcomePreprocessingProducer.sendSpgOutcomeToPreprocessingQueue(spgOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewUnitAddress(SPGNewUnitAddress newUnitAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_SPG_UNITADDRESS_OUTCOME_RECEIVED,
        TRANSACTION_ID, newUnitAddress.getTransactionId().toString(),
        SURVEY_TYPE, "SPG",
        PRIMARY_OUTCOME, newUnitAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, newUnitAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, newUnitAddress.getOutcomeCode(),
        "SPGNewUnitAddress", newUnitAddress.toString());

    outcomePreprocessingProducer.sendSpgNewUnitAddressToPreprocessingQueue(newUnitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewStandalone(SPGNewStandaloneAddress newStandaloneAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_SPG_STANDALONE_OUTCOME_RECEIVED,
        TRANSACTION_ID, newStandaloneAddress.getTransactionId().toString(),
        SURVEY_TYPE, "SPG",
        PRIMARY_OUTCOME, newStandaloneAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, newStandaloneAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, newStandaloneAddress.getOutcomeCode(),
        "SPGNewStandaloneAddress", newStandaloneAddress.toString());

    outcomePreprocessingProducer.sendSpgNewStandaloneAddress(newStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhOutcomeResponse(String caseID, HHOutcome hhOutcome) {
    gatewayEventManager.triggerEvent(caseID, COMET_HH_OUTCOME_RECEIVED,
        TRANSACTION_ID, hhOutcome.getTransactionId().toString(),
        SURVEY_TYPE, "HH",
        PRIMARY_OUTCOME, hhOutcome.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, hhOutcome.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, hhOutcome.getOutcomeCode(),
        "HHOutcome", hhOutcome.toString());

    outcomePreprocessingProducer.sendHHOutcomeToPreprocessingQueue(hhOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhNewSplitAddress(HHNewSplitAddress hhNewSplitAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_HH_SPLITADDRESS_RECEIVED,
        TRANSACTION_ID, hhNewSplitAddress.getTransactionId().toString(),
        SURVEY_TYPE, "HH",
        PRIMARY_OUTCOME, hhNewSplitAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, hhNewSplitAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, hhNewSplitAddress.getOutcomeCode(),
        "HHNewSplitAddress", hhNewSplitAddress.toString());

    outcomePreprocessingProducer.sendHHSplitAddressToPreprocessingQueue(hhNewSplitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhNewStandalone(HHNewStandaloneAddress hhNewStandaloneAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_HH_STANDALONE_RECEIVED,
        TRANSACTION_ID, hhNewStandaloneAddress.getTransactionId().toString(),
        SURVEY_TYPE, "HH",
        PRIMARY_OUTCOME, hhNewStandaloneAddress.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, hhNewStandaloneAddress.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, hhNewStandaloneAddress.getOutcomeCode(),
        "HHNewStandaloneAddress", hhNewStandaloneAddress.toString());

    outcomePreprocessingProducer.sendHHStandaloneAddressToPreprocessingQueue(hhNewStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ccsPropertyListing(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    gatewayEventManager.triggerEvent(String.valueOf(ccsPropertyListingOutcome.getCaseId()), COMET_CCS_PL_RECEIVED,
        TRANSACTION_ID, ccsPropertyListingOutcome.getTransactionId().toString(),
        SURVEY_TYPE, "CCS PL",
        PRIMARY_OUTCOME, ccsPropertyListingOutcome.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, ccsPropertyListingOutcome.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, ccsPropertyListingOutcome.getOutcomeCode());

    outcomePreprocessingProducer.sendCcsPropertyListingToPreprocessingQueue(ccsPropertyListingOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ccsInterview(String caseID, CCSInterviewOutcome ccsInterviewOutcome) {
    gatewayEventManager.triggerEvent(caseID, COMET_CCS_INT_RECEIVED,
        TRANSACTION_ID, ccsInterviewOutcome.getTransactionId().toString(),
        SURVEY_TYPE, "CCS INT",
        PRIMARY_OUTCOME, ccsInterviewOutcome.getPrimaryOutcomeDescription(),
        SECONDARY_OUTCOME, ccsInterviewOutcome.getSecondaryOutcomeDescription(),
        OUTCOME_CODE, ccsInterviewOutcome.getOutcomeCode());

    ccsInterviewOutcome.setCaseId(UUID.fromString(caseID));
    outcomePreprocessingProducer.sendCcsInterviewToPreprocessingQueue(ccsInterviewOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
