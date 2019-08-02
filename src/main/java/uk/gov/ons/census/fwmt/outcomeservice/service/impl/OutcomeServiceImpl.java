package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HHOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private List<HHOutcomeServiceProcessor> householdOutcomeConverters;

  @Autowired
  private List<CcsOutcomeServiceProcessor> propertyListingConverters;

  @Autowired
  private List<InterviewOutcomeServiceProcessor> interviewOutcomeConverters;

  @PostConstruct
  public void init() {
  }

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) {
    for (HHOutcomeServiceProcessor converter : householdOutcomeConverters) {
      if (converter.isValid(householdOutcome)) {
        try {
          converter.processMessage(householdOutcome);
        } catch (GatewayException e) {
          log.error("failed to convert outcome", e);
        }
      }
    }
  }

  public void createPropertyListingOutcomeEvent(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    for (CcsOutcomeServiceProcessor converter : propertyListingConverters) {
      if (converter.isValid(ccsPropertyListingOutcome)) {
        try {
          converter.processMessage(ccsPropertyListingOutcome);
        } catch (GatewayException e) {
          log.error("failed to convert outcome", e);
        }
      }
    }
  }

  @Override
  public void createInterviewOutcomeEvent(CCSInterviewOutcome ccsInterviewOutcome) {
    for (InterviewOutcomeServiceProcessor converter : interviewOutcomeConverters) {
      if (converter.isValid(ccsInterviewOutcome)) {
        try {
          converter.processMessage(ccsInterviewOutcome);
        } catch (GatewayException e) {
          log.error("failed to convert outcome", e);
        }
      }
    }
  }
}
