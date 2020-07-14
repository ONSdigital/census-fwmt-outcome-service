package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.ActionInstructionType;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtActionInstruction;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.RmFieldRepublishProducer;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.common.data.tm.SurveyType.CE_EST_F;
import static uk.gov.ons.census.fwmt.common.data.tm.SurveyType.CE_UNIT_F;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.RM_FIELD_REPUBLISH;

@Component("SWITCH_FEEDBACK_CE_UNIT_F")
public class SwitchFeedbackUnitFProcessor implements OutcomeServiceProcessor {

  @Autowired
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();

    gatewayEventManager.triggerEvent(String.valueOf(caseId), PROCESSING_OUTCOME,
    "survey type", type,
    "processor", "SWITCH_FEEDBACK_CE_UNIT_F",
    "original caseId", String.valueOf(outcome.getCaseId()));

    FwmtActionInstruction fieldworkFollowup = FwmtActionInstruction.builder()
        .actionInstruction(ActionInstructionType.SWITCH_CE_TYPE)
        .surveyName("CENSUS")
        .addressType(type)
        .surveyType(CE_UNIT_F)
        .caseId(caseId.toString())
        .build();

    rmFieldRepublishProducer.republish(fieldworkFollowup);

    gatewayEventManager.triggerEvent(String.valueOf(caseId), RM_FIELD_REPUBLISH,
        "survey name", "CENSUS",
        "address type", type,
        "survey type", CE_UNIT_F.toString(),
    "action instruction", ActionInstructionType.SWITCH_CE_TYPE.toString(),
    "transactionId", outcome.getTransactionId().toString());

    return caseId;
  }
}
