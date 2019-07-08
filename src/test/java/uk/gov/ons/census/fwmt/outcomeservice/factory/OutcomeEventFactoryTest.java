package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.helper.HouseholdOutcomeBuilder;
import uk.gov.ons.ctp.common.error.CTPException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutcomeEventFactoryTest {

  @InjectMocks
  private OutcomeEventFactory outcomeEventFactory;

  @Mock
  private BuildSecondaryOutcomeMaps buildSecondaryOutcomeMaps;

//  @Test
//  public void createContactMadeRefusalTest () throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdHardRefusal();
//
//    // When
//    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);
//
//    // Then
//    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getRefusal().getCollectionCase().getId());
//    assertEquals("HARD_REFUSAL", outcomeEvents.getPayload().getRefusal().getType());
//  }
//
//  @Test
//  public void createContactMadeHouseholdSplitAddressTest () throws GatewayException, CTPException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdSplitAddress();
//
//    // When
//    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);
//
//    // Then
//    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
//    assertEquals("SPLIT_ADDRESS", outcomeEvents.getPayload().getInvalidAddress().getReason());
//  }
//
//  @Test(expected = GatewayException.class)
//  public void createContactMadeHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdIncorrectSecondaryRequest();
//
//    // When
//    outcomeEventFactory.createOutcomeEvent(householdOutcome);
//  }
//
//  @Test
//  public void createNoValidHouseholdDerelictTest () throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdDerelict();
//
//    // When
//    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);
//
//    // Then
//    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
//    assertEquals("DERELICT", outcomeEvents.getPayload().getInvalidAddress().getReason());
//  }
//
//  @Test(expected = GatewayException.class)
//  public void createNoValidHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdIncorrectSecondaryRequest();
//
//    // When
//    outcomeEventFactory.createOutcomeEvent(householdOutcome);
//  }
//
//  @Test(expected = GatewayException.class)
//  public void createNoValidHouseholdIncorrectPrimaryRequestTest () throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdIncorrectPrimaryRequest();
//
//    // When
//    outcomeEventFactory.createOutcomeEvent(householdOutcome);
//  }
//
//  @Test
//  public void createCENoValidAddressContactMade() throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createCENoValidAddressContactMade();
//
//    // When
//    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);
//
//    // Then
//    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getCollectionCase().getId());
//    assertEquals("CE",
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getAddressType());
//    assertEquals(householdOutcome.getCeDetails().getEstablishmentType(),
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getEstabType());
//    assertEquals(householdOutcome.getCeDetails().getEstablishmentName(),
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getOrgName());
//    assertEquals(householdOutcome.getCeDetails().getManagerTitle(), outcomeEvents.getPayload().getContact().getTitle());
//    assertEquals(householdOutcome.getCeDetails().getManagerForename(),
//        outcomeEvents.getPayload().getContact().getForename());
//    assertEquals(householdOutcome.getCeDetails().getManagerSurname(),
//        outcomeEvents.getPayload().getContact().getSurname());
//    assertEquals(householdOutcome.getCeDetails().getContactPhone(), outcomeEvents.getPayload().getContact().getTelNo());
//    assertEquals(householdOutcome.getCeDetails().getUsualResidents().longValue(),
//        outcomeEvents.getPayload().getCollectionCase().getCeExpectedResponses());
//
//  }
//
//  @Test
//  public void createCENoValidAddressNoContactMade() throws GatewayException {
//    //  Given
//    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createCENoValidAddressNoContactMade();
//
//    // When
//    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);
//
//    // Then
//    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getCollectionCase().getId());
//    assertEquals("CE",
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getAddressType());
//    assertEquals(householdOutcome.getCeDetails().getEstablishmentType(),
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getEstabType());
//    assertEquals(householdOutcome.getCeDetails().getEstablishmentName(),
//        outcomeEvents.getPayload().getCollectionCase().getAddress().getOrgName());
//    assertNull(outcomeEvents.getPayload().getContact().getTitle());
//  }
}
