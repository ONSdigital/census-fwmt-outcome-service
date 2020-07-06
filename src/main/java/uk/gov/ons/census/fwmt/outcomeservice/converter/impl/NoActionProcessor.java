package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RECEIVED_NO_ACTION_FROM_TM;

@Component("NO_ACTION")
public class NoActionProcessor implements OutcomeServiceProcessor {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    gatewayEventManager
        .triggerErrorEvent(this.getClass(), (Exception) null, "Action not expected",
            RECEIVED_NO_ACTION_FROM_TM, "NO ACTION",
            "Transaction id", String.valueOf(outcome.getTransactionId()),
            "Primary Outcome", outcome.getPrimaryOutcomeDescription(),
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription());

    return outcome.getCaseId();
  }
}
