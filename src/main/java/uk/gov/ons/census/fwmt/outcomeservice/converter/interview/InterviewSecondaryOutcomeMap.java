package uk.gov.ons.census.fwmt.outcomeservice.converter.interview;

import java.util.HashMap;
import java.util.Map;

public class InterviewSecondaryOutcomeMap {

  static final Map<String, String> interviewSecondaryOutcomeMap = new HashMap<>();

  {
    // REFUSAL RECEIVED
    interviewSecondaryOutcomeMap.put("Extraordinary refusal", "EXTRAORDINARY_REFUSAL");
    interviewSecondaryOutcomeMap.put("Hard refusal", "HARD_REFUSAL");

    // ADDRESS NOT VALID
    interviewSecondaryOutcomeMap.put("Property not in postcode boundary", "CCS_NOT_IN_POSTCODE_BOUNDARY");
    interviewSecondaryOutcomeMap.put("Split address", "SPLIT_ADDRESS");
    interviewSecondaryOutcomeMap.put("Property is a CE", "CCS_HOUSEHOLD_CASE_IS_A_CE");
    interviewSecondaryOutcomeMap.put("Derelict", "DERELICT");
    interviewSecondaryOutcomeMap.put("Non-residential or business", "NON_RESIDENTIAL");
    interviewSecondaryOutcomeMap.put("Demolished", "DEMOLISHED");
    interviewSecondaryOutcomeMap.put("Duplicate", "DUPLICATE");
    interviewSecondaryOutcomeMap.put("Under construction", "UNDER_CONSTRUCTION");
    interviewSecondaryOutcomeMap.put("Incorrect address", "CCS_INCORRECT_ADDRESS");
    interviewSecondaryOutcomeMap.put("CE out of scope", "CCS_CE_OUT_OF_SCOPE");
    interviewSecondaryOutcomeMap.put("Property is a household", "CCS_CE_CASE_IS_A_HOUSEHOLD");
  }

  InterviewSecondaryOutcomeMap() {
  }
}
