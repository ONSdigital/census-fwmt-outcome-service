package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;

@Component
public class BuildSecondaryOutcomeMaps {

  public void buildSecondaryOutcomeMap() {
    // Non Valid Household
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Derelict", "DERELICT");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Demolished", "DEMOLISHED");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Can't Find", "CANT_FIND");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Unaddressable Object", "UNADDRESSABLE_OBJECT");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Non-residential", "NON_RESIDENTIAL");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Duplicate", "DUPLICATE");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Under Construction", "UNDER_CONSTRUCTION");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Property is a CE - no contact made", "PROPERTY_IS_CE_NO_CONTACT_MADE");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Property is a CE - Contact made", "PROPERTY_IS_CE_CONTACT_MADE");

    // Contact Made
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Split Address", "SPLIT_ADDRESS");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Hard Refusal", "HARD_REFUSAL");
    OutcomeEventFactory.noValidHouseholdOutcomeMap.put("Extraordinary Refusal", "EXTRAORDINARY_REFUSAL");
  }
}
