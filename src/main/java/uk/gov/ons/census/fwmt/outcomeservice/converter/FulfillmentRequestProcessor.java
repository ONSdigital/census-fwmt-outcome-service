package uk.gov.ons.census.fwmt.outcomeservice.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import javax.annotation.Nonnull;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.ctp.integration.common.product.model.Product.CaseType.HI;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Slf4j
@Component
public class FulfillmentRequestProcessor implements OutcomeServiceProcessor {

  @Autowired
  private ProductReference productReference;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    List<String> invalidSecondaryOutcomes = Arrays
        .asList("Split address", "Hard refusal", "Extraordinary refusal");
    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && !invalidSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) {
    for (FulfillmentRequest fulfillmentRequest : householdOutcome.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfillmentRequest)) {
        createQuestionnaireRequiredByPostEvent(householdOutcome, fulfillmentRequest);
        log.error("Failed to find valid Secondary Outcome: {}", householdOutcome.getSecondaryOutcome());
      }
    }
  }

  private void sendToFulfillmentQueue(HouseholdOutcome householdOutcome, String outcomeEvent) {
    try {
      gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
      gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
    } catch (GatewayException e) {
      e.printStackTrace();
    }
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId() != null);
  }

  private void createQuestionnaireRequiredByPostEvent(HouseholdOutcome householdOutcome,
      FulfillmentRequest fulfillmentRequest) {
    Product product = getProductFromQuestionnaireType(fulfillmentRequest);
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("productCodeLookup", product.getFulfilmentCode());
    root.put("telNo", fulfillmentRequest.getRequesterPhone());

    if (product.getCaseType().equals(HI)) {
      root.put("householdIndicator", 0);
      root.put("individualCaseId", generateUUID());
    } else {
      root.put("householdIndicator", 1);

    }
    String outcomeEvent = TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root);

    sendToFulfillmentQueue(householdOutcome, outcomeEvent);
  }

  private String generateUUID() {
    String generatedUUID;
    generatedUUID = String.valueOf(UUID.randomUUID());
    return generatedUUID;
  }

  @Nonnull
  private Product getProductFromQuestionnaireType(FulfillmentRequest fulfillmentRequest) {
    Product product = new Product();
    List<Product.RequestChannel> requestChannels = Collections.singletonList(FIELD);

    product.setRequestChannels(requestChannels);
    product.setFieldQuestionnaireCode(fulfillmentRequest.getQuestionnaireType());

    List<Product> productList = null;
    try {
      productList = productReference.searchProducts(product);
    } catch (CTPException e) {
      log.error("unable to find valid Products {}", e);
    }
    return Objects.requireNonNull(productList).get(0);
  }
}
