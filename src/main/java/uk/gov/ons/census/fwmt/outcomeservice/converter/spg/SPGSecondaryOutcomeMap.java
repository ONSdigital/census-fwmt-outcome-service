package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import java.util.HashMap;
import java.util.Map;

class SPGSecondaryOutcomeMap {

  static final Map<String, String> spgSecondaryOutcomeMap = new HashMap<>();

  {
    // REFUSAL
    spgSecondaryOutcomeMap.put("Phone - Hard Refusal", "HARD_REFUSAL");
    spgSecondaryOutcomeMap.put("Phone - Extraordinary Refusal", "EXTRAORDINARY_REFUSAL");

    // ADDRESS NOT VALID
    spgSecondaryOutcomeMap.put("Visit - Unoccupied Site", "UNOCCUPIED_SITE");
    spgSecondaryOutcomeMap.put("Phone - Unoccupied Site", "UNOCCUPIED_SITE");
    spgSecondaryOutcomeMap.put("Unit unoccupied", "UNOCCUPIED_UNIT");
    spgSecondaryOutcomeMap.put("Phone - Derelict / demolished", "DERELICT-DEMOLISHED");
    spgSecondaryOutcomeMap.put("Visit - Derelict / demolished", "DERELICT-DEMOLISHED");
    spgSecondaryOutcomeMap.put("Phone - Non-Residential", "NON-RESIDENTIAL");
    spgSecondaryOutcomeMap.put("Visit - Non-Residential", "NON-RESIDENTIAL");
    spgSecondaryOutcomeMap.put("Phone - Incorrect Address", "INCORRECT_ADDRESS");
    spgSecondaryOutcomeMap.put("Visit - Incorrect address", "INCORRECT_ADDRESS");
    spgSecondaryOutcomeMap.put("Phone - Under construction", "UNDER_CONSTRUCTION");
    spgSecondaryOutcomeMap.put("Visit - Under construction", "UNDER_CONSTRUCTION");
    spgSecondaryOutcomeMap.put("Visit - Can't find property", "CANT_FIND_PROPERTY");
    spgSecondaryOutcomeMap.put("Visit - Unaddressable Object", "UNADDRESSABLE");
    spgSecondaryOutcomeMap.put("Visit - Duplicate", "DUPLICATE");
  }

  SPGSecondaryOutcomeMap(){

  }
}
