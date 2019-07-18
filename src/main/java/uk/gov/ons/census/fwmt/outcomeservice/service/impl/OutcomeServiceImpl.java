package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
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

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) {

    for (OutcomeServiceProcessor converter : converters) {
      if (converter.isValid(householdOutcome)) {
        converter.processMessage(householdOutcome);
      }
    }
  }

  @Override
  public <T> void ccsPropertyListingOutcomeEvent(T outcome) {




  }

  public void ccsPropertyListingOutcomeEvent(CCSPropertyListingOutcome ccsPropertyListingOutcome) {


  }
}
