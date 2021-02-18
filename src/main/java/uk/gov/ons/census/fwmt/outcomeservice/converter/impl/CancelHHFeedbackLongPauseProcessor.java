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

import static uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceLogConfig.*;

@Component("FEEDBACK_LONG_PAUSE")
public class CancelHHFeedbackLongPauseProcessor implements OutcomeServiceProcessor {

    @Autowired
    private RmFieldRepublishProducer rmFieldRepublishProducer;

    @Autowired
    private GatewayEventManager gatewayEventManager;

    @Override
    public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
        UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

        gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
            SURVEY_TYPE, type,
            PROCESSOR, "FEEDBACK_LONG_PAUSE",
            ORIGINAL_CASE_ID, String.valueOf(outcome.getCaseId()));

        FwmtCancelActionInstruction fieldworkCancel = FwmtCancelActionInstruction.builder()
                .actionInstruction(ActionInstructionType.CANCEL)
                .surveyName("CENSUS")
                .addressType("HH")
                .addressLevel("U")
                .caseId(String.valueOf(outcome.getCaseId()))
                .build();

        rmFieldRepublishProducer.republish(fieldworkCancel);

        gatewayEventManager.triggerEvent(String.valueOf(caseId), RM_FIELD_REPUBLISH,
            SURVEY_NAME, "CENSUS",
            ADDRESS_TYPE, "HH",
            ACTION_INSTRUCTION_TYPE, ActionInstructionType.CANCEL.toString(),
            TRANSACTION_ID, outcome.getTransactionId().toString());

        return caseId;
    }
}
