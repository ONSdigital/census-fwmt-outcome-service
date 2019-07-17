package uk.gov.ons.census.fwmt.outcomeservice.enums;

public enum SecondaryOutcomes {

  DERELICT("Derelict"),
  DEMOLISHED("Demolished"),
  CANT_FIND("Cant find"),
  UNADDRESSABLE_OBJECT("Unaddressable Object"),
  NON_RESIDENTIAL("Non-res"),
  DUPLICATE("Duplicate"),
  UNDER_CONSTRUCTION("Under Const");

  private String value;

  SecondaryOutcomes(String value) {
    this.value = value;
  }

}
