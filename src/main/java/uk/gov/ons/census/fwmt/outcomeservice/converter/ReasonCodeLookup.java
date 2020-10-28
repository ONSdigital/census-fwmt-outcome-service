package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.HashMap;
import java.util.Map;

public class ReasonCodeLookup {

  private final Map<String, String> spgReasonCodeMap = new HashMap<>();

  public String getLookup(String outcomeCode) {
    if (spgReasonCodeMap.get(outcomeCode) == null) {
      return "NOT_FOUND";
    } else {
      return spgReasonCodeMap.get(outcomeCode);
    }
  }

  public void add(String productCode, String processorNames) {
    spgReasonCodeMap.put(productCode, processorNames);
  }

}
