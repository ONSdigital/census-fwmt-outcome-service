package uk.gov.ons.census.fwmt.outcomeservice.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefusalEncryptionLookup {

  private final Map<String, String> refusalCodeMap = new HashMap<>();

  public String getLookup(String refusal) {
    return refusalCodeMap.get(refusal);
  }

  public void add (String refusalSelector, String refusal) {
    refusalCodeMap.put(refusalSelector, refusal);
  }

}

