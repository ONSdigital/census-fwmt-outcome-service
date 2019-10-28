package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import java.util.HashMap;
import java.util.Map;

class CcsSecondaryOutcomeMap {

  static final Map<String, String> ccsSecondaryOutcomeMap = new HashMap<>();

  {
    ccsSecondaryOutcomeMap.put("Derelict / Uninhabitable", "DERELICT");
    ccsSecondaryOutcomeMap.put("Under construction", "UNDER_CONSTRUCTION");
    ccsSecondaryOutcomeMap.put("Non residential or business", "NON_RESIDENTIAL");
    ccsSecondaryOutcomeMap.put("CE Out of scope", "CCS_CE_OUT_OF_SCOPE");
    ccsSecondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");
    ccsSecondaryOutcomeMap.put("Soft refusal", "SOFT_REFUSAL");
    ccsSecondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");
    ccsSecondaryOutcomeMap.put("Non-residential or business", "NON_RESIDENTIAL");
  }

  CcsSecondaryOutcomeMap() {
  }
}
