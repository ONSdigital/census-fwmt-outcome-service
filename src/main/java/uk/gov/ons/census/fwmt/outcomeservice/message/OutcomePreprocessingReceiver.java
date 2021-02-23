package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HHNewSplitAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHOutcome;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.UUID;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  public static final String PREPROCESSING_HH_OUTCOME = "PREPROCESSING_HH_OUTCOME";

  public static final String PREPROCESSING_HH_SPLITADDRESS_OUTCOME = "PREPROCESSING_HH_SPLITADDRESS_OUTCOME";

  public static final String PREPROCESSING_HH_STANDALONE_OUTCOME = "PREPROCESSING_HH_STANDALONE_OUTCOME";

  public static final String PREPROCESSING_CE_OUTCOME = "PREPROCESSING_CE_OUTCOME";

  public static final String PREPROCESSING_CE_UNITADDRESS_OUTCOME = "PREPROCESSING_CE_UNITADDRESS_OUTCOME";

  public static final String PREPROCESSING_CE_STANDALONE_OUTCOME = "PREPROCESSING_CE_STANDALONE_OUTCOME";

  public static final String PREPROCESSING_SPG_OUTCOME = "PREPROCESSING_SPG_OUTCOME";

  public static final String PREPROCESSING_SPG_UNITADDRESS_OUTCOME = "PREPROCESSING_SPG_UNITADDRESS_OUTCOME";

  public static final String PREPROCESSING_SPG_STANDALONE_OUTCOME = "PREPROCESSING_SPG_STANDALONE_OUTCOME";

  public static final String PREPROCESSING_CCS_PL_OUTCOME = "PREPROCESSING_CCS_PL_OUTCOME";

  public static final String PREPROCESSING_CCS_INT_OUTCOME = "PREPROCESSING_CCS_INT_OUTCOME";

  public static final String PREPROCESSING_NC_OUTCOME = "PREPROCESSING_NC_OUTCOME";

  @Autowired
  private OutcomeService delegate;

  @Autowired
  private MapperFacade mapperFacade;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    triggerEvent(spgOutcome, PREPROCESSING_SPG_OUTCOME, "SPG");
    delegate.createSpgOutcomeEvent(spgOutcome);
  }

  public void processMessage(SPGNewUnitAddress newUnitAddress) throws GatewayException {
    newUnitAddress.setCaseId(UUID.randomUUID());
    triggerEvent(newUnitAddress, PREPROCESSING_SPG_UNITADDRESS_OUTCOME, "SPG");
    delegate.createSpgOutcomeEvent(newUnitAddress);
  }

  public void processMessage(SPGNewStandaloneAddress standaloneAddress) throws GatewayException {
    //TODO CONFIRM THE CASEID IS BEING MAPPED IN THE NEXT LEVEL
    standaloneAddress.setCaseId(UUID.randomUUID());
    triggerEvent(standaloneAddress, PREPROCESSING_SPG_STANDALONE_OUTCOME, "SPG");
    delegate.createSpgOutcomeEvent(standaloneAddress);
  }

  public void processMessage(CEOutcome ceOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(ceOutcome, OutcomeSuperSetDto.class);
    triggerEvent(ceOutcome, PREPROCESSING_CE_OUTCOME, "CE");
    delegate.createCeOutcomeEvent(ceOutcome);
  }

  public void processMessage(CENewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    triggerEvent(newUnitAddress, PREPROCESSING_CE_UNITADDRESS_OUTCOME, "CE");
    delegate.createCeOutcomeEvent(newUnitAddress);
  }

  public void processMessage(CENewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    triggerEvent(standaloneAddress, PREPROCESSING_CE_STANDALONE_OUTCOME, "CE");
    delegate.createCeOutcomeEvent(standaloneAddress);
  }

  public void processMessage(HHOutcome hhOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(hhOutcome, OutcomeSuperSetDto.class);
    triggerEvent(hhOutcome, PREPROCESSING_HH_OUTCOME, "HH");
    delegate.createHhOutcomeEvent(hhOutcome);
  }

  public void processMessage(HHNewSplitAddress splitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(splitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    triggerEvent(splitAddress, PREPROCESSING_HH_SPLITADDRESS_OUTCOME, "HH");
    delegate.createHhOutcomeEvent(splitAddress);
  }

  public void processMessage(HHNewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    triggerEvent(standaloneAddress, PREPROCESSING_HH_STANDALONE_OUTCOME, "HH");
    delegate.createHhOutcomeEvent(standaloneAddress);
  }

  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException {
    triggerEvent(ccsPropertyListingOutcome, PREPROCESSING_CCS_PL_OUTCOME, "CCS PL");
    delegate.createCcsPropertyListingOutcomeEvent(ccsPropertyListingOutcome);
  }

  public void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    triggerEvent(ccsInterviewOutcome, PREPROCESSING_CCS_INT_OUTCOME, "CCS INT");
    delegate.createCcsInterviewOutcomeEvent(ccsInterviewOutcome);
  }

  public void processMessage(NCOutcome ncOutcome) throws GatewayException {
    triggerEvent(ncOutcome, PREPROCESSING_NC_OUTCOME, "NC");
    delegate.createNcOutcomeEvent(ncOutcome);
  }

  private void triggerEvent(CommonOutcome commonOutcome, String eventType, String surveyType) {
    gatewayEventManager.triggerEvent(String.valueOf(commonOutcome.getCaseId()), eventType,
        "Survey type", surveyType,
        "Outcome code", commonOutcome.getOutcomeCode(),
        "Secondary Outcome", commonOutcome.getSecondaryOutcomeDescription());
  }
}
