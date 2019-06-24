package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;

@Component
public class BuildSecondaryOutcomeMaps {

  public void buildSecondaryOutcomeMap() {
    // Non Valid Household
    OutcomeEventFactory.secondaryOutcomeMap.put("Derelict", "DERELICT");
    OutcomeEventFactory.secondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    OutcomeEventFactory.secondaryOutcomeMap.put("Can't Find", "CANT_FIND");
    OutcomeEventFactory.secondaryOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    OutcomeEventFactory.secondaryOutcomeMap.put("Non-residential", "NON_RESIDENTIAL");
    OutcomeEventFactory.secondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    OutcomeEventFactory.secondaryOutcomeMap.put("Under Construction", "UNDER_CONSTRUCTION");

    // Contact Made
    OutcomeEventFactory.secondaryOutcomeMap.put("Split Address", "SPLIT_ADDRESS");
    OutcomeEventFactory.secondaryOutcomeMap.put("Hard Refusal", "HARD_REFUSAL");
    OutcomeEventFactory.secondaryOutcomeMap.put("Extraordinary Refusal", "EXTRAORDINARY_REFUSAL");
  }
}
