package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import java.util.HashMap;
import java.util.Map;

class HhSecondaryOutcomeMap {

  static final Map<String, String> householdSecondaryOutcomeMap = new HashMap<>();

  {
    // Non Valid Household
    householdSecondaryOutcomeMap.put("Derelict", "DERELICT");
    householdSecondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    householdSecondaryOutcomeMap.put("Cant find", "CANT_FIND");
    householdSecondaryOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    householdSecondaryOutcomeMap.put("Non-res", "NON_RESIDENTIAL");
    householdSecondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    householdSecondaryOutcomeMap.put("Under Const", "UNDER_CONSTRUCTION");
    householdSecondaryOutcomeMap.put("CE - Contact made", "CE_CONTACT_MADE");
    householdSecondaryOutcomeMap.put("CE - No contact", "CE_NO_CONTACT_MADE");

    // Contact Made
    householdSecondaryOutcomeMap.put("Split address", "SPLIT_ADDRESS");
    householdSecondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");
    householdSecondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");
  }

  HhSecondaryOutcomeMap() {
  }
}
