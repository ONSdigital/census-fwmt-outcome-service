package uk.gov.ons.census.fwmt.feedbackservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.feedbackservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.census.fwmt.feedbackservice.service.FeedbackService;

@RestController
@RequestMapping("/CensusCaseOutcome")
public class CensusCaseOutcomeController {

  @Autowired
  private FeedbackService feedbackService;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void censusCaseOutcomeResponse(@RequestBody CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    feedbackService.sendFeedback(censusCaseOutcomeDTO);
  }
}
