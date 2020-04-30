package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.ActionInstructionType;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtActionInstruction;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtCancelActionInstruction;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.RmFieldRepublishProducer;

import java.util.UUID;

@Slf4j
@Component("CANCEL_FEEDBACK")
public class SpgCancelFeedbackProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    FwmtCancelActionInstruction fieldworkFollowup = FwmtCancelActionInstruction.builder()
        .actionInstruction(ActionInstructionType.CANCEL)
        .surveyName("CENSUS")
        .addressType("SPG")
        .addressLevel("U")
        .caseId(outcome.getCaseId().toString())
        .build();

    rmFieldRepublishProducer.republish(fieldworkFollowup);

    return null;
  }
}
