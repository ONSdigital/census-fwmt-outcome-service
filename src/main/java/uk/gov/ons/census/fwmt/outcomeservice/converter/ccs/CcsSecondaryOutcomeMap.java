package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import java.util.HashMap;
import java.util.Map;

class CcsSecondaryOutcomeMap {

  static final Map<String, String> ccsSecondaryOutcomeMap = new HashMap<>();

  {
    ccsSecondaryOutcomeMap.put("Derelict/ uninhabitable", "DERELICT");
    ccsSecondaryOutcomeMap.put("Under construction", "UNDER_CONSTRUCTION");
    ccsSecondaryOutcomeMap.put("Non residential or business", "NON_RESIDENTIAL");
    ccsSecondaryOutcomeMap.put("CE Out of scope", "CCS_CE_OUT_OF_SCOPE");
  }
}
