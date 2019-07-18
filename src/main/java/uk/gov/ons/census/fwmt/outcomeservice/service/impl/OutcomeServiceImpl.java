package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private List<OutcomeServiceProcessor> converters;

  @PostConstruct
  public void init() {
  }

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {

    for (OutcomeServiceProcessor converter : converters) {
      if (converter.isValid(householdOutcome)) {
        converter.processMessage(householdOutcome);
      }
    }
  }
}
