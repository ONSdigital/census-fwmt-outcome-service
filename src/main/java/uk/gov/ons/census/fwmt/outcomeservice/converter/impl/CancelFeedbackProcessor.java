package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.ActionInstructionType;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtCancelActionInstruction;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.RmFieldRepublishProducer;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RM_FIELD_REPUBLISH;

@Slf4j
@Component("CANCEL_FEEDBACK")
public class CancelFeedbackProcessor implements OutcomeServiceProcessor {

  @Autowired
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
    "survey type", type,
    "processor", "CANCEL_FEEDBACK",
    "original caseId", String.valueOf(outcome.getCaseId()));

    FwmtCancelActionInstruction fieldworkFollowup = FwmtCancelActionInstruction.builder()
        .actionInstruction(ActionInstructionType.CANCEL)
        .surveyName("CENSUS")
        .addressType(type)
        .addressLevel("U")
        .caseId(caseId.toString())
        .build();

    rmFieldRepublishProducer.republish(fieldworkFollowup);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), RM_FIELD_REPUBLISH,
        "survey name", "CENSUS",
        "address type", type,
    "action instruction", ActionInstructionType.CANCEL.toString(),
    "transactionId", outcome.getTransactionId().toString());

    return caseId;
  }
}
