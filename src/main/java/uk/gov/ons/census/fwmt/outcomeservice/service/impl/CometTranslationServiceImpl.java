package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.service.CometTranslationService;

import static uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.InvalidAddress.ReasonEnum.DERELICT;

@Service
public class CometTranslationServiceImpl implements CometTranslationService {

  public void transformCometPayload(HouseholdOutcome householdOutcome) {

    OutcomeEvent outcomeEvent = new OutcomeEvent();

    // set the outcomeEvent CaseId
    outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().setId(householdOutcome.getCaseId());

    //Check the primary outcome type:
    // could use a switch statement dealing with strings and cases
    // take into consideration other outcomes
    // noValidHousehold
    // if primaryOutcome = noValidHousehold
    //    check secondaryOutcome
    //      if secondaryOutcome = derelict
    //      if secondaryOutcome = demolished
    //      etc..
    //
    // contactMade

    // Interrogate householdOutcome object for primary and secondary outcome
    buildOutcome(householdOutcome, outcomeEvent);
  }

  private void buildOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) {
    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      getSecondaryNoValidHouseholdOutcome(householdOutcome, outcomeEvent);
      break;
    case "Contact Made":
      // code block
      break;
    default:
      // code block
    }
  }

  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome,
      OutcomeEvent outcomeEvent) {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "derelict":
      outcomeEvent.getPayload().getInvalidAddress().setReason(DERELICT);
      break;
    case "demolished":
      // code block
      break;
    default:
      // code block
    }
  }
}
