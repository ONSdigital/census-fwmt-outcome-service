package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import java.util.HashMap;
import java.util.Map;

public class SpgSecondaryOutcomeMap {

  private Map<String, String> spgSecondaryOutcomeMap = new HashMap<>();

  {
    spgSecondaryOutcomeMap.put("Phone - Hard Refusal", "HARD_REFUSAL");
    spgSecondaryOutcomeMap.put("Phone - Extraordinary Refusal", "EXTRAORDINARY_REFUSAL");
  }

  public String getLookup(String secondaryOutcome) {
    return spgSecondaryOutcomeMap.get(secondaryOutcome);
  }

  SpgSecondaryOutcomeMap(){

  }
}
