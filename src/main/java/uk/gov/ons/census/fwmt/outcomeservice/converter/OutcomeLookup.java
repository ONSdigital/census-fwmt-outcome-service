package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.HashMap;
import java.util.Map;

public class OutcomeLookup {

  private final Map<String, String[]> spgOutcomeCodeMap = new HashMap<>();

  public String[] getLookup(String outcomeCode) {
    return spgOutcomeCodeMap.get(outcomeCode);
  }

  public void add(String productCode, String[] processorNames) {
    spgOutcomeCodeMap.put(productCode, processorNames);
  }

}

