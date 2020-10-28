package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.HashMap;
import java.util.Map;

public class ReasonCodeLookup {

  private final Map<String, String> reasonCodeLookupMap = new HashMap<>();

  public String getLookup(String outcomeCode) {
    if (reasonCodeLookupMap.get(outcomeCode) == null) {
      return "NOT_FOUND";
    } else {
      return reasonCodeLookupMap.get(outcomeCode);
    }
  }

  public void add(String productCode, String processorNames) {
    reasonCodeLookupMap.put(productCode, processorNames);
  }

}
