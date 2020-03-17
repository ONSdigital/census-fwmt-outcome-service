package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SPGOutcomeLookup;

import java.util.Map;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private Map<String, SPGOutcomeServiceProcessor> spgOutcomeServiceProcessors;

  public void receiveSpgOutcome(SPGOutcome spgOutcome) throws GatewayException {
    String[] operationsList = SPGOutcomeLookup.getLookup(spgOutcome.getOutcomeCode());
    for (String operation : operationsList) {
      spgOutcomeServiceProcessors.get(operation).processMessageSpgOutcome(spgOutcome);
    }
  }

  public void receiveNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    String[] operationsList = SPGOutcomeLookup.getLookup(newUnitAddress.getOutcomeCode());
    for (String operation : operationsList) {
      spgOutcomeServiceProcessors.get(operation).processMessageNewUnitAddress(newUnitAddress);
    }
  }

  public void receiveStandaloneAddress(NewStandaloneAddress standaloneAddress) throws GatewayException {
    String[] operationsList = SPGOutcomeLookup.getLookup(standaloneAddress.getOutcomeCode());
    for (String operation : operationsList) {
      spgOutcomeServiceProcessors.get(operation).processMessageNewStandaloneAddress(standaloneAddress);
    }
  }
}
