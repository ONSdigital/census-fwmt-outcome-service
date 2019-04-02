package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.service.CometTranslationService;

@Service
public class CometTranslationServiceImpl implements CometTranslationService {

  public void transformCometPayload(HouseholdOutcome householdOutcome) {

    String caseId = householdOutcome.getCaseId();

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

    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      getSecondaryNoValidHouseholdOutcome(householdOutcome);
      break;
    case "Contact Made":
      // code block
      break;
    default:
      // code block
    }
  }

  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome) {
    switch (householdOutcome.getSecondaryOutcome()) {
    case "derelict":
      // code block
      break;
    case "demolished":
      // code block
      break;
    default:
      // code block
    }
  }
}
