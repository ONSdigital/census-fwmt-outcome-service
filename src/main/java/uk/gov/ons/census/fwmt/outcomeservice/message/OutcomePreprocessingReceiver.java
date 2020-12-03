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

  private static final String SURVEY_TYPE = "Survey Type";

  private static final String SECONDARY_OUTCOME = "Secondary Outcome";

  private static final String OUTCOME_CODE = "Outcome Code";

  @Autowired
  private OutcomeService delegate;

  @Autowired
  private MapperFacade mapperFacade;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(spgOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_OUTCOME,
        SURVEY_TYPE, "SPG",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(SPGNewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_UNITADDRESS_OUTCOME,
        SURVEY_TYPE, "SPG",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(SPGNewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_STANDALONE_OUTCOME,
        SURVEY_TYPE, "SPG",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CEOutcome ceOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(ceOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_OUTCOME,
        SURVEY_TYPE, "CE",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CENewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_UNITADDRESS_OUTCOME,
        SURVEY_TYPE, "CE",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CENewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_STANDALONE_OUTCOME,
        SURVEY_TYPE, "CE",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }

  public void processMessage(HHOutcome hhOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(hhOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_HH_OUTCOME,
        SURVEY_TYPE, "HH",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createHhOutcomeEvent(outcomeDTO);
  }

  public void processMessage(HHNewSplitAddress splitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(splitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_HH_SPLITADDRESS_OUTCOME,
        SURVEY_TYPE, "HH",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createHhOutcomeEvent(outcomeDTO);
  }

  public void processMessage(HHNewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_HH_STANDALONE_OUTCOME,
        SURVEY_TYPE, "HH",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createHhOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(ccsPropertyListingOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CCS_PL_OUTCOME,
        SURVEY_TYPE, "CCS PL",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCcsPropertyListingOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(ccsInterviewOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CCS_INT_OUTCOME,
        SURVEY_TYPE, "CCS INT",
        OUTCOME_CODE, outcomeDTO.getOutcomeCode(),
        SECONDARY_OUTCOME, outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCcsInterviewOutcomeEvent(outcomeDTO);
  }
}
