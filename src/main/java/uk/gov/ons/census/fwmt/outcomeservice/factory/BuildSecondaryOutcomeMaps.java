package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;

@Component
public class BuildSecondaryOutcomeMaps {

  public void buildSecondaryOutcomeMap() {
    // Non Valid Household
    OutcomeEventFactory.secondaryOutcomeMap.put("Derelict", "DERELICT");
    OutcomeEventFactory.secondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    OutcomeEventFactory.secondaryOutcomeMap.put("Cant find", "CANT_FIND");
    OutcomeEventFactory.secondaryOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    OutcomeEventFactory.secondaryOutcomeMap.put("Non-res", "NON_RESIDENTIAL");
    OutcomeEventFactory.secondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    OutcomeEventFactory.secondaryOutcomeMap.put("Under Const", "UNDER_CONSTRUCTION");

    // Contact Made
    OutcomeEventFactory.secondaryOutcomeMap.put("Split address", "SPLIT_ADDRESS");
    OutcomeEventFactory.secondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");
    OutcomeEventFactory.secondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");
  }
}
