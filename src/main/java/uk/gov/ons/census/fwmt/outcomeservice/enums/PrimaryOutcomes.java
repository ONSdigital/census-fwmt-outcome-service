package uk.gov.ons.census.fwmt.outcomeservice.enums;

public enum PrimaryOutcomes {

  NON_VALID_HOUSEHOLD("Non-valid household"),
  CONTACT_MADE("Contact made"),
  NOT_VALID("Not Valid"),
  NO_CONTACT("No contact");

  private String value;

  PrimaryOutcomes(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
