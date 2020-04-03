package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private OutcomeService delegate;

  @Autowired
  private MapperFacade mapperFacade;

  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    SpgOutcomeSuperSetDto outcomeDTO = mapperFacade.map(spgOutcome, SpgOutcomeSuperSetDto.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewUnitAddress newUnitAddress) throws GatewayException {
    SpgOutcomeSuperSetDto outcomeDTO = mapperFacade.map(newUnitAddress, SpgOutcomeSuperSetDto.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewStandaloneAddress standaloneAddress) throws GatewayException {
    SpgOutcomeSuperSetDto outcomeDTO = mapperFacade.map(standaloneAddress, SpgOutcomeSuperSetDto.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }
}
