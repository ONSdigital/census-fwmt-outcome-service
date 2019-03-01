package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@RestController
@RequestMapping("/CensusCaseOutcome")
public class CensusCaseOutcomeController {

  @Autowired
  private OutcomeService outcomeService;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void censusCaseOutcomeResponse(@RequestBody CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    outcomeService.sendOutcome(censusCaseOutcomeDTO);
  }
}
