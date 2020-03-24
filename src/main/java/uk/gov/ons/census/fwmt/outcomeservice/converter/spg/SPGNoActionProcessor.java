package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RECEIVED_NO_ACTION_FROM_TM;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;

@Component("NO_ACTION")
public class SPGNoActionProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException {
    gatewayEventManager
    .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
        RECEIVED_NO_ACTION_FROM_TM,
        "Transaction id", String.valueOf(outcome.getTransactionId()),
        "Primary Outcome", outcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
        outcome.getSecondaryOutcomeDescription());

    return outcome.getCaseId();
  }
}
