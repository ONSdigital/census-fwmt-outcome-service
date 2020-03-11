package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;

import java.util.HashMap;
import java.util.Map;

@Component
public class SPGReasonCodeLookup {

  private static final Map<String, String> spgReasonCodeMap = new HashMap<>();

  public void add(String productCode, String processorNames) {
    spgReasonCodeMap.put(productCode, processorNames);
  }

  public static String getLookup(SPGOutcome spgOutcome) {
    return spgReasonCodeMap.get(spgOutcome.getOutcomeCode());
  }
}
