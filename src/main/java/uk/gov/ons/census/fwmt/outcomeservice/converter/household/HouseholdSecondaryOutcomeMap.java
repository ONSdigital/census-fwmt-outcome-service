package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import java.util.HashMap;
import java.util.Map;

class HouseholdSecondaryOutcomeMap {

  static final Map<String, String> householdSecondaryOutcomeMap = new HashMap<>();

  private HouseholdSecondaryOutcomeMap() {
  }

  {
    // Non Valid Household
    householdSecondaryOutcomeMap.put("Derelict", "DERELICT");
    householdSecondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    householdSecondaryOutcomeMap.put("Cant find", "CANT_FIND");
    householdSecondaryOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    householdSecondaryOutcomeMap.put("Non-res", "NON_RESIDENTIAL");
    householdSecondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    householdSecondaryOutcomeMap.put("Under Const", "UNDER_CONSTRUCTION");

    // Contact Made
    householdSecondaryOutcomeMap.put("Split address", "SPLIT_ADDRESS");
    householdSecondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");
    householdSecondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");
  }
}
