package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SPGOutcomeLookup {

  private static final Map<String, String[]> spgOutcomeCodeMap = new HashMap<>();

  public static String[] getLookup(String outcomeCode) {
    return spgOutcomeCodeMap.get(outcomeCode);
  }

  public void add(String productCode, String[] processorNames) {
    spgOutcomeCodeMap.put(productCode, processorNames);
  }

}

