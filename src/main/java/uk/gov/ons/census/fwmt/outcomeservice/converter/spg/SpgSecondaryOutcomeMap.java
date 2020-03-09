package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import java.util.HashMap;
import java.util.Map;

class SpgSecondaryOutcomeMap {

  static final Map<String, String> spgSecondaryOutcomeMap = new HashMap<>();

  {
    spgSecondaryOutcomeMap.put("Phone - Hard Refusal", "HARD_REFUSAL");
    spgSecondaryOutcomeMap.put("Phone - Extraordinary Refusal", "EXTRAORDINARY_REFUSAL");
  }

  SpgSecondaryOutcomeMap(){

  }
}
