package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FulfilmentRequestFactoryTest {

  @InjectMocks
  private FulfilmentRequestFactory fulfilmentRequestFactory;

  @Mock
  private ProductReference productReference;

  @Test
  public void createFulfillmentRequestsForPaperHTest() throws GatewayException, CTPException {
    // Given
    String productCode = "P_OR_H1";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperH();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setCaseType(Product.CaseType.HH);
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperHCTest() throws GatewayException, CTPException {
    // Given
    String productCode = "P_OR_HC1";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperHC();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setCaseType(Product.CaseType.HH);
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperITest() throws GatewayException, CTPException {
    // Given
    String productCode = "P_OR_I1";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperI();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setCaseType(Product.CaseType.HH);
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForPaperIncorrectQuestionnaireTypeTest() throws GatewayException, CTPException {
    // Given
    String productCode = "IncorrectTypeID";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForPaperWithIncorrectQuestionnaireType();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setCaseType(Product.CaseType.HH);
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForHUACTextBackTest() throws GatewayException, CTPException {
    // Given
    String productCode = "UACHHT1";

    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForHUACTextBack();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackTest() throws GatewayException, CTPException {
    // Given
    String productCode = "UACIT1";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForIUACTextBack();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getFulfillment().getCaseId());
    assertEquals(productCode, outcomeEvents[0].getPayload().getFulfillment().getProductCode());
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackNoNumberTest() throws GatewayException, CTPException {
    // Given
    String productCode = "IUAC1";
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForIUACTextBackWithNoNumber();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForIUACTextBackWithIncorrectQuestionnaireTypeTest()
      throws GatewayException, CTPException {
    // Given
    String productCode = "IncorrectTypeID";

    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForIUACTextBackWithIncorrectQuestionnaireType();

    List<Product> resultProductList = new ArrayList<>();
    Product resultProduct = new Product();
    resultProduct.setFulfilmentCode(productCode);
    resultProductList.add(resultProduct);

    Mockito.when(productReference.searchProducts(Mockito.any())).thenReturn(resultProductList);

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

  @Test
  public void createFulfillmentRequestsForWillCompleteTest() throws GatewayException {
    // Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeForWillComplete();

    // When
    OutcomeEvent[] outcomeEvents = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

    // Then
    assertEquals(householdOutcome.getCaseId(), outcomeEvents[0].getPayload().getUac().getCaseId());
    assertEquals(householdOutcome.getFulfillmentRequests().get(0).getQuestionnaireId(), outcomeEvents[0].getPayload().getUac().getQuestionnaireId());
  }

  @Test
  public void createFulfillmentRequestsWithNoSecondaryRequestTest() throws GatewayException {
    // Given
    HouseholdOutcome householdOutcome = new HouseholdOutcomeBuilder().createHouseholdOutcomeWithNoSecondaryRequest();

    // When
    fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);
  }

}
