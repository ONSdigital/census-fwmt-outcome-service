package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CESPGSTANDALONE_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CESPGUNITADDRESS_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PREPROCESSING_CESPG_OUTCOME;

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
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CESPG_OUTCOME,
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewUnitAddress newUnitAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CESPGUNITADDRESS_OUTCOME,
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewStandaloneAddress standaloneAddress) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, OutcomeSuperSetDto.class);
    outcomeDTO.setCaseId(UUID.randomUUID());
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CESPGSTANDALONE_OUTCOME,
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(CEOutcome CeOutcome) throws GatewayException {
    OutcomeSuperSetDto outcomeDTO = mapperFacade.map(CeOutcome, OutcomeSuperSetDto.class);
    gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), PREPROCESSING_CESPG_OUTCOME,
        "Outcome code", outcomeDTO.getOutcomeCode(),
        "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription());
    delegate.createCeOutcomeEvent(outcomeDTO);
  }
}
