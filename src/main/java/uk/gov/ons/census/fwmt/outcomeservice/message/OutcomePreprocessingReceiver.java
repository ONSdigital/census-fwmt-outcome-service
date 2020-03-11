package uk.gov.ons.census.fwmt.outcomeservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private OutcomeService outcomeService;

  public void receiveMessage(SPGOutcome spgOutcome) throws GatewayException {
    log.info("Received a message in Outcome queue");
    outcomeService.createSpgOutcomeEvent(spgOutcome);
  }
}
