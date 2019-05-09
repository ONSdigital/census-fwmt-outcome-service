package uk.gov.ons.census.fwmt.outcomeservice.data.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class FulfilmentRequestMappingTest {

  FulfilmentRequestMapping fulfilmentRequestMapping = new FulfilmentRequestMapping();

  @Test
  public void getHouseholdPaperRequestedEnglish() {
    System.out.println(fulfilmentRequestMapping.getHouseholdPaperRequestedEnglish());
  }

  @Test
  public void getHouseholdPaperRequestedEnglishPackCode() {
    System.out.println(fulfilmentRequestMapping.getHouseholdPaperRequestedEnglish());
  }
}