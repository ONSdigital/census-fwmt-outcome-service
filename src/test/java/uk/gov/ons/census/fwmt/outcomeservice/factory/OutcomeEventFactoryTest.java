package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.helper.HouseholdOutcomeBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutcomeEventFactoryTest {

  @InjectMocks
  OutcomeEventFactory outcomeEventFactory = new OutcomeEventFactory();

  @Test
  public void createContactMadeRefusalTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createContactMadeHouseholdHardRefusal();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createContactMadeHouseholdHardRefusal();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getRefusal().getCollectionCase().getId());
    assertEquals(householdOutcome.getSecondaryOutcome(), outcomeEvents.getPayload().getRefusal().getType());
  }

  @Test
  public void createContactMadeHouseholdSplitAddressTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createContactMadeHouseholdSplitAddress();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createContactMadeHouseholdSplitAddress();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
    assertEquals(householdOutcome.getSecondaryOutcome(), outcomeEvents.getPayload().getInvalidAddress().getReason());
  }

  @Test(expected = GatewayException.class)
  public void createContactMadeHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createContactMadeHouseholdIncorrectSecondaryRequest();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createContactMadeHouseholdIncorrectSecondaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

  @Test
  public void createNoValidHouseholdDerelictTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createNoValidHouseholdDerelict();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createNoValidHouseholdDerelict();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
    assertEquals(householdOutcome.getSecondaryOutcome(), outcomeEvents.getPayload().getInvalidAddress().getReason());
  }

  @Test(expected = GatewayException.class)
  public void createNoValidHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createNoValidHouseholdIncorrectSecondaryRequest();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createNoValidHouseholdIncorrectSecondaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

  @Test(expected = GatewayException.class)
  public void createNoValidHouseholdIncorrectPrimaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createNoValidHouseholdIncorrectPrimaryRequest();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createNoValidHouseholdIncorrectPrimaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

}
