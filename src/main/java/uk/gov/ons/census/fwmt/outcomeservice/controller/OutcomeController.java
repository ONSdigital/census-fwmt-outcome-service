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
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.SwitchCaseIdService;

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
  private static final String COMET_NC_OUTCOME_RECEIVED = "COMET_NC_OUTCOME_RECEIVED";

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomePreprocessingProducer outcomePreprocessingProducer;

  @Autowired
  private SwitchCaseIdService switchCaseIdService;

  @Override
  public ResponseEntity<Void> ceOutcomeResponse(String caseId, CEOutcome ceOutcome) {
    gatewayEventManager.triggerEvent(caseId, COMET_CE_OUTCOME_RECEIVED,
        "transactionId", ceOutcome.getTransactionId().toString(),
        "Primary Outcome", ceOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", ceOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", ceOutcome.getOutcomeCode(),
        "CEOutcome", ceOutcome.toString());
    ceOutcome.setCaseId(UUID.fromString(caseId));

    outcomePreprocessingProducer.sendCeOutcomeToPreprocessingQueue(ceOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ceNewUnitAddress(CENewUnitAddress newUnitAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_CE_UNITADDRESS_OUTCOME_RECEIVED,
        "transactionId", newUnitAddress.getTransactionId().toString(),
        "Site case id", String.valueOf(newUnitAddress.getSiteCaseId()),
        "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", newUnitAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newUnitAddress.getOutcomeCode(),
        "CENewUnitAddress", newUnitAddress.toString());

    outcomePreprocessingProducer.sendCeNewUnitAddressToPreprocessingQueue(newUnitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ceNewStandalone(CENewStandaloneAddress newStandaloneAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_CE_STANDALONE_OUTCOME_RECEIVED,
        "transactionId", newStandaloneAddress.getTransactionId().toString(),
        "Survey type", "CE",
        "Primary Outcome", newStandaloneAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", newStandaloneAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newStandaloneAddress.getOutcomeCode(),
        "CENewStandaloneAddress", newStandaloneAddress.toString());

    outcomePreprocessingProducer.sendCeNewStandaloneAddressToPreprocessingQueue(newStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgOutcomeResponse(String caseId, SPGOutcome spgOutcome) {
    gatewayEventManager.triggerEvent(caseId, COMET_SPG_OUTCOME_RECEIVED,
        "transactionId", spgOutcome.getTransactionId().toString(),
        "Survey type", "SPG",
        "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", spgOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", spgOutcome.getOutcomeCode(),
        "SPGOutcome", spgOutcome.toString());

    spgOutcome.setCaseId(UUID.fromString(caseId));
    outcomePreprocessingProducer.sendSpgOutcomeToPreprocessingQueue(spgOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewUnitAddress(SPGNewUnitAddress newUnitAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_SPG_UNITADDRESS_OUTCOME_RECEIVED,
        "transactionId", newUnitAddress.getTransactionId().toString(),
        "Survey type", "SPG",
        "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", newUnitAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newUnitAddress.getOutcomeCode(),
        "SPGNewUnitAddress", newUnitAddress.toString());

    outcomePreprocessingProducer.sendSpgNewUnitAddressToPreprocessingQueue(newUnitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> spgNewStandalone(SPGNewStandaloneAddress newStandaloneAddress) {
    gatewayEventManager.triggerEvent("N/A", COMET_SPG_STANDALONE_OUTCOME_RECEIVED,
        "transactionId", newStandaloneAddress.getTransactionId().toString(),
        "Survey type", "SPG",
        "Primary Outcome", newStandaloneAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", newStandaloneAddress.getSecondaryOutcomeDescription(),
        "Outcome code", newStandaloneAddress.getOutcomeCode(),
        "SPGNewStandaloneAddress", newStandaloneAddress.toString());

    outcomePreprocessingProducer.sendSpgNewStandaloneAddress(newStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhOutcomeResponse(String caseID, HHOutcome hhOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseID, COMET_HH_OUTCOME_RECEIVED,
        "transactionId", hhOutcome.getTransactionId().toString(),
        "Survey type", "HH",
        "Primary Outcome", hhOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", hhOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", hhOutcome.getOutcomeCode(),
        "HHOutcome", hhOutcome.toString());

    outcomePreprocessingProducer.sendHHOutcomeToPreprocessingQueue(hhOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhNewSplitAddress(HHNewSplitAddress hhNewSplitAddress) throws GatewayException {
    gatewayEventManager.triggerEvent("N/A", COMET_HH_SPLITADDRESS_RECEIVED,
        "transactionId", hhNewSplitAddress.getTransactionId().toString(),
        "Survey type", "HH",
        "Primary Outcome", hhNewSplitAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", hhNewSplitAddress.getSecondaryOutcomeDescription(),
        "Outcome code", hhNewSplitAddress.getOutcomeCode(),
        "HHNewSplitAddress", hhNewSplitAddress.toString());

    outcomePreprocessingProducer.sendHHSplitAddressToPreprocessingQueue(hhNewSplitAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> hhNewStandalone(HHNewStandaloneAddress hhNewStandaloneAddress)
      throws GatewayException {
    gatewayEventManager.triggerEvent("N/A", COMET_HH_STANDALONE_RECEIVED,
        "transactionId", hhNewStandaloneAddress.getTransactionId().toString(),
        "Survey type", "HH",
        "Primary Outcome", hhNewStandaloneAddress.getPrimaryOutcomeDescription(),
        "Secondary Outcome", hhNewStandaloneAddress.getSecondaryOutcomeDescription(),
        "Outcome code", hhNewStandaloneAddress.getOutcomeCode(),
        "HHNewStandaloneAddress", hhNewStandaloneAddress.toString());

    outcomePreprocessingProducer.sendHHStandaloneAddressToPreprocessingQueue(hhNewStandaloneAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ccsPropertyListing(CCSPropertyListingOutcome ccsPropertyListingOutcome)
      throws GatewayException {
    gatewayEventManager.triggerEvent(String.valueOf(ccsPropertyListingOutcome.getCaseId()), COMET_CCS_PL_RECEIVED,
        "transactionId", ccsPropertyListingOutcome.getTransactionId().toString(),
        "Survey type", "CCS PL",
        "Primary Outcome", ccsPropertyListingOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", ccsPropertyListingOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", ccsPropertyListingOutcome.getOutcomeCode(),
        "CCSPropertyListing", ccsPropertyListingOutcome.toString());

    outcomePreprocessingProducer.sendCcsPropertyListingToPreprocessingQueue(ccsPropertyListingOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ccsInterview(String caseID, CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    gatewayEventManager.triggerEvent(caseID, COMET_CCS_INT_RECEIVED,
        "transactionId", ccsInterviewOutcome.getTransactionId().toString(),
        "Survey type", "CCS INT",
        "Primary Outcome", ccsInterviewOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", ccsInterviewOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", ccsInterviewOutcome.getOutcomeCode(),
        "CCSInterview", ccsInterviewOutcome.toString());

    ccsInterviewOutcome.setCaseId(UUID.fromString(caseID));
    outcomePreprocessingProducer.sendCcsInterviewToPreprocessingQueue(ccsInterviewOutcome);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> ncOutcome(String caseID, NCOutcome ncOutcome) throws GatewayException {
    String hhCaseId = switchCaseIdService.fromNcToOriginal(caseID);
    ncOutcome.setCaseId(UUID.fromString(hhCaseId));
    gatewayEventManager.triggerEvent(caseID, COMET_NC_OUTCOME_RECEIVED,
        "transactionId", ncOutcome.getTransactionId().toString(),
        "Original HH CaseId", hhCaseId,
        "Survey type", "NC",
        "Primary Outcome", ncOutcome.getPrimaryOutcomeDescription(),
        "Secondary Outcome", ncOutcome.getSecondaryOutcomeDescription(),
        "Outcome code", ncOutcome.getOutcomeCode(),
        "NCOutcome", ncOutcome.toString());
    outcomePreprocessingProducer.sendHHStandaloneAddressToPreprocessingQueue(ncOutcome);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
