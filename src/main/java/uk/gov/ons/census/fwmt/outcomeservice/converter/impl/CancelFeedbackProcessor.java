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
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.SwitchCaseIdService;

import java.util.UUID;

@Slf4j
@Component("CANCEL_FEEDBACK")
public class CancelFeedbackProcessor implements OutcomeServiceProcessor {

  @Autowired
  private SwitchCaseIdService switchCaseIdService;

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String RM_FIELD_REPUBLISH = "RM_FIELD_REPUBLISH";

  @Autowired
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();
    String ncCaseId = null;
    if (type.equals("NC")) {
      ncCaseId = switchCaseIdService.fromIdOriginalToNc(String.valueOf(caseId));
    }

    String loggedCaseId = ncCaseId != null ? ncCaseId : String.valueOf(caseId);
    gatewayEventManager.triggerEvent(loggedCaseId, PROCESSING_OUTCOME,
        "survey type", type,
        "processor", "CANCEL_FEEDBACK",
        "original caseId", String.valueOf(outcome.getCaseId()),
        "Site Case id", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));

    FwmtCancelActionInstruction fieldworkFollowup = FwmtCancelActionInstruction
        .builder()
        .actionInstruction(ActionInstructionType.CANCEL)
        .surveyName("FEEDBACK")
        .addressType("FEEDBACK")
        .addressLevel("F")
        .caseId(loggedCaseId)
        .build();

    rmFieldRepublishProducer.republish(fieldworkFollowup);

    gatewayEventManager.triggerEvent(loggedCaseId, RM_FIELD_REPUBLISH,
        "survey name", "CENSUS",
        "address type", type,
        "original caseId", String.valueOf(outcome.getCaseId()),
        "action instruction", ActionInstructionType.CANCEL.toString(),
        "transactionId", outcome.getTransactionId().toString());
    return caseId;
  }
}
