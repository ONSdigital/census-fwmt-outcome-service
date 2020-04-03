package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.FieldworkFollowup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.RmFieldRepublishService;

import java.util.UUID;

@Slf4j
@Component("DELIVERED_FEEDBACK")
public class SpgDeliveredFeedbackProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  RmFieldRepublishService rmFieldRepublishService;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    FieldworkFollowup fieldworkFollowup = FieldworkFollowup.builder()
        .actionInstruction("UPDATE")
        .surveyName("CENSUS")
        .addressType("SPG")
        .addressLevel("U")
        .caseId(outcome.getCaseId().toString())
        .build();

    rmFieldRepublishService.republish(fieldworkFollowup);

    return null;
  }
}
