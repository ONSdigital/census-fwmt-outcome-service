package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RECEIVED_NO_ACTION_FROM_TM;

@Component("NO_ACTION")
public class SpgNoActionProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    gatewayEventManager
        .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
            RECEIVED_NO_ACTION_FROM_TM,
            "Transaction id", String.valueOf(outcome.getTransactionId()),
            "Primary Outcome", outcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
            outcome.getSecondaryOutcomeDescription());

    return outcome.getCaseId();
  }
}
