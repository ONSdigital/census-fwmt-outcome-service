package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SPGReasonCodeLookup {

  private final Map<String, String> spgReasonCodeMap = new HashMap<>();

  public String getLookup(String outcomeCode) {
    return spgReasonCodeMap.get(outcomeCode);
  }

  public void add(String productCode, String processorNames) {
    spgReasonCodeMap.put(productCode, processorNames);
  }

}
