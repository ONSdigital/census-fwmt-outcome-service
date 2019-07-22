package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.HashMap;
import java.util.Map;

class BuildSecondaryOutcomeMaps {

  static final Map<String, String> secondaryOutcomeMap = new HashMap<>();

  {
    // Non Valid Household
    secondaryOutcomeMap.put("Derelict", "DERELICT");
    secondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    secondaryOutcomeMap.put("Cant find", "CANT_FIND");
    secondaryOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    secondaryOutcomeMap.put("Non-res", "NON_RESIDENTIAL");
    secondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    secondaryOutcomeMap.put("Under Const", "UNDER_CONSTRUCTION");

    // Contact Made
    secondaryOutcomeMap.put("Split address", "SPLIT_ADDRESS");
    secondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");
    secondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");

    // CCS
    secondaryOutcomeMap.put("Derelict/ uninhabitable", "DERELICT");
    secondaryOutcomeMap.put("Under construction", "UNDER_CONSTRUCTION");
    secondaryOutcomeMap.put("Non residential or business", "NON_RESIDENTIAL");
    secondaryOutcomeMap.put("CE Out of scope", "CCS_CE_OUT_OF_SCOPE");
  }
}
