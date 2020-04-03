package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HhOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.InterviewOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SpgOutcomeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private List<HhOutcomeServiceProcessor> householdOutcomeConverters;

  @Autowired
  private List<CcsOutcomeServiceProcessor> propertyListingConverters;

  @Autowired
  private List<InterviewOutcomeServiceProcessor> interviewOutcomeConverters;

  @Autowired
  private Map<String, SpgOutcomeServiceProcessor> spgOutcomeServiceProcessors;

  @Autowired
  private SpgOutcomeLookup spgOutcomeLookup;

  @Override
  public void createSpgOutcomeEvent(SpgOutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = spgOutcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null)
      return;
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      caseIdHolder = spgOutcomeServiceProcessors.get(operation).process(outcome, caseIdHolder);
    }
  }

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) {
    for (HhOutcomeServiceProcessor converter : householdOutcomeConverters) {
      if (converter.isValid(householdOutcome)) {
        try {
          converter.processMessage(householdOutcome);
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
          log.error("failed to convert outcome", e);
        }
      }
    }
  }

}
