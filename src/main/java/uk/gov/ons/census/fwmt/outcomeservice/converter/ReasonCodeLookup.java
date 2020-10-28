package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.HashMap;
import java.util.Map;

public class ReasonCodeLookup {

  private final Map<String, String> reasonCodeLookupMap = new HashMap<>();

  public String getLookup(String outcomeCode) {
    return reasonCodeLookupMap.get(outcomeCode) != null ? reasonCodeLookupMap.get(outcomeCode) : "NOT_FOUND";
  }

  public void add(String productCode, String processorNames) {
    reasonCodeLookupMap.put(productCode, processorNames);
  }

}
