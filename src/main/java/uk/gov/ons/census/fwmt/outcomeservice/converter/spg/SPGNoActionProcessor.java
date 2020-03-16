package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RECEIVED_NO_ACTION_FROM_TM;

@Component("NO_ACTION")
public class SPGNoActionProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessageSpgOutcome(SPGOutcome spgOutcome) {
    gatewayEventManager
        .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
            RECEIVED_NO_ACTION_FROM_TM,
            "Transaction id", String.valueOf(spgOutcome.getTransactionId()),
            "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
            spgOutcome.getSecondaryOutcomeDescription());
  }

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) {
    gatewayEventManager
        .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
            RECEIVED_NO_ACTION_FROM_TM,
            "Transaction id", String.valueOf(newUnitAddress.getTransactionId()),
            "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(), "Secondary Outcome",
            newUnitAddress.getSecondaryOutcomeDescription());
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress) {
    gatewayEventManager
        .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
            RECEIVED_NO_ACTION_FROM_TM,
            "Transaction id", String.valueOf(newStandaloneAddress.getTransactionId()),
            "Primary Outcome", newStandaloneAddress.getPrimaryOutcomeDescription(), "Secondary Outcome",
            newStandaloneAddress.getSecondaryOutcomeDescription());
  }
}
