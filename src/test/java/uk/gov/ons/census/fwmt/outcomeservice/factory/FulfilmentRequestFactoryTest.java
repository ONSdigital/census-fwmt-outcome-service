package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.outcomeservice.helper.HouseholdOutcomeBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FulfilmentRequestFactoryTest {

  @InjectMocks
  FulfilmentRequestFactory fulfilmentRequestFactory;

  @Mock
  BuildFulfilmentRequestMaps buildFulfilmentRequestMaps;

  @Test
  public void createFulfillmentRequestsForPaperHTest() {
    // Given
    String productCode = "P_OR_H1";
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForPaperH();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForPaperH();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperHCTest() {
    // Given
    String productCode = "P_OR_HC1";
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForPaperHC();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForPaperHC();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperITest() {
    // Given
    String productCode = "P_OR_I1";
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForPaperI();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForPaperI();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperIncorrectQuestionnaireTypeTest() {
    // Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForPaperWithIncorrectQuestionnaireType();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForPaperWithIncorrectQuestionnaireType();

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForHUACTextBackTest() {
    // Given
    String productCode = "UACHHT1";
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForHUACTextBack();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForHUACTextBack();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackTest() {
    // Given
    String productCode = "UACIT1";
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBack();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBack();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackNoNumberTest() {
    // Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBackWithNoNumber();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBackWithNoNumber();

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackWithIncorrectQuestionnaireTypeTest() {
    // Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBackWithIncorrectQuestionnaireType();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForIUACTextBackWithIncorrectQuestionnaireType();

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForWillCompleteTest() {
    // Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeForWillComplete();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeForWillComplete();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getUac().getCaseId());
    assertEquals(householdOutcome.getFulfillmentRequests().get(0).getQuestionnaireId(), outcomeEvents[0].getPayload().getUac().getQuestionnaireId());
  }

  @Test
  public void createFulfillmentRequestsWithNoSecondaryRequestTest() {
    // Given
    HouseholdOutcomeBuilder householdOutcomeBuilder = new HouseholdOutcomeBuilder();
    householdOutcomeBuilder.createHouseholdOutcomeWithNoSecondaryRequest();

    HouseholdOutcome householdOutcome = householdOutcomeBuilder.createHouseholdOutcomeWithNoSecondaryRequest();

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

}
