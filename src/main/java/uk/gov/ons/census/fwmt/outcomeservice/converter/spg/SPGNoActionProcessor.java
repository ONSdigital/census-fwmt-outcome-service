package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RECEIVED_NO_ACTION_FROM_TM;

@Component("NO_ACTION")
public class SPGNoActionProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {

    gatewayEventManager
        .triggerErrorEvent(this.getClass(), null, "Action not expected", spgOutcome.getCaseReference(),
            RECEIVED_NO_ACTION_FROM_TM, "Transaction id", String.valueOf(spgOutcome.getTransactionId()),
            "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
            spgOutcome.getSecondaryOutcomeDescription());

  }
}
