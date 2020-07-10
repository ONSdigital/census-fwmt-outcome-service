package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CE_STANDALONE_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CE_UNITADDRESS_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_SPG_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_SPG_STANDALONE_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_SPG_UNITADDRESS_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CE_OUTCOME;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private OutcomeService delegate;

  @Autowired
  private MapperFacade mapperFacade;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(spgOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_OUTCOME,
        "Survey type", "SPG",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(SPGNewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_UNITADDRESS_OUTCOME,
        "Survey type", "SPG",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(SPGNewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_SPG_STANDALONE_OUTCOME,
        "Survey type", "SPG",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CEOutcome CeOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(CeOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_OUTCOME,
        "Survey type", "CE",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CENewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_UNITADDRESS_OUTCOME,
        "Survey type", "CE",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CENewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CE_STANDALONE_OUTCOME,
        "Survey type", "CE",
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }
}
