package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

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

@Component("FEEDBACK_LONG_PAUSE")
public class CancelHHFeedbackLongPauseProcessor implements OutcomeServiceProcessor {

    @Autowired
    private RmFieldRepublishProducer rmFieldRepublishProducer;

    @Autowired
    private GatewayEventManager gatewayEventManager;

    public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

    public static final String RM_FIELD_REPUBLISH = "RM_FIELD_REPUBLISH";

    @Override
    public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
        UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

        gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
                "survey type", type,
                "processor", "FEEDBACK_LONG_PAUSE",
                "original caseId", String.valueOf(outcome.getCaseId()),
                "Site Case id", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));

        FwmtCancelActionInstruction fieldworkCancel = FwmtCancelActionInstruction.builder()
                .actionInstruction(ActionInstructionType.CANCEL)
                .surveyName("CENSUS")
                .addressType("HH")
                .addressLevel("U")
                .caseId(String.valueOf(outcome.getSiteCaseId()))
                .build();

        rmFieldRepublishProducer.republish(fieldworkCancel);

        gatewayEventManager.triggerEvent(String.valueOf(caseId), RM_FIELD_REPUBLISH,
                "survey name", "CENSUS",
                "address type", "HH",
                "action instruction", ActionInstructionType.CANCEL.toString(),
                "transactionId", outcome.getTransactionId().toString());

        return caseId;
    }
}
