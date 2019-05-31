package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.helper.HouseholdOutcomeBuilder;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutcomeEventFactoryTest {

  @InjectMocks
  private OutcomeEventFactory outcomeEventFactory;

  @Mock
  private BuildSecondaryOutcomeMaps buildSecondaryOutcomeMaps;

  @Test
  public void createContactMadeRefusalTest () throws GatewayException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdHardRefusal();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getRefusal().getCollectionCase().getId());
    assertEquals("HARD_REFUSAL", outcomeEvents.getPayload().getRefusal().getType());
  }

  @Test
  public void createContactMadeHouseholdSplitAddressTest () throws GatewayException, CTPException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdSplitAddress();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
    assertEquals("SPLIT_ADDRESS", outcomeEvents.getPayload().getInvalidAddress().getReason());
  }

  @Test(expected = GatewayException.class)
  public void createContactMadeHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createContactMadeHouseholdIncorrectSecondaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

  @Test
  public void createNoValidHouseholdDerelictTest () throws GatewayException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdDerelict();

    // When
    OutcomeEvent outcomeEvents = outcomeEventFactory.createOutcomeEvent(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents.getPayload().getInvalidAddress().getCollectionCase().getId());
    assertEquals("DERELICT", outcomeEvents.getPayload().getInvalidAddress().getReason());
  }

  @Test(expected = GatewayException.class)
  public void createNoValidHouseholdIncorrectSecondaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdIncorrectSecondaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

  @Test(expected = GatewayException.class)
  public void createNoValidHouseholdIncorrectPrimaryRequestTest () throws GatewayException {
    //  Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createNoValidHouseholdIncorrectPrimaryRequest();

    // When
    outcomeEventFactory.createOutcomeEvent(householdOutcome);
  }

}
