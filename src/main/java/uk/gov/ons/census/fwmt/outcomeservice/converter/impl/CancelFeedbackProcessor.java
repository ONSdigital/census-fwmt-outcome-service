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

@Slf4j
@Component("CANCEL_FEEDBACK")
public class CancelFeedbackProcessor implements OutcomeServiceProcessor {

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String RM_FIELD_REPUBLISH = "RM_FIELD_REPUBLISH";

  @Autowired
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
    "Survey type", type,
    "Processor", "CANCEL_FEEDBACK",
    "Case ID", String.valueOf(outcome.getCaseId()),
    "Site Case ID", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));

    FwmtCancelActionInstruction fieldworkFollowup = FwmtCancelActionInstruction.builder()
        .actionInstruction(ActionInstructionType.CANCEL)
        .surveyName("FEEDBACK")
        .addressType("FEEDBACK")
        .addressLevel("F")
        .caseId(caseId.toString())
        .build();

    rmFieldRepublishProducer.republish(fieldworkFollowup);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), RM_FIELD_REPUBLISH,
        "Survey name", "CENSUS",
        "Address type", type,
        "Action instruction", ActionInstructionType.CANCEL.toString(),
        "Transaction ID", outcome.getTransactionId().toString());
    return caseId;
  }
}
