package uk.gov.ons.census.fwmt.outcomeservice.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private OutcomeService delegate;

  @Autowired
  private MapperFacade mapperFacade;

  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    SPGOutcomeSuperSetDTO outcomeDTO = mapperFacade.map(spgOutcome, SPGOutcomeSuperSetDTO.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewUnitAddress newUnitAddress) throws GatewayException {
    SPGOutcomeSuperSetDTO outcomeDTO = mapperFacade.map(newUnitAddress, SPGOutcomeSuperSetDTO.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }

  public void processMessage(NewStandaloneAddress standaloneAddress) throws GatewayException {
    SPGOutcomeSuperSetDTO outcomeDTO = mapperFacade.map(standaloneAddress, SPGOutcomeSuperSetDTO.class);
    delegate.createSpgOutcomeEvent(outcomeDTO);
  }
}
