package uk.gov.ons.census.fwmt.outcomeservice.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.comet.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Component
@Slf4j
public class FulfilmentRequestFactory {

  @Autowired
  private ProductReference productReference;

  public List<String> createFulfilmentRequestEvent(HouseholdOutcome householdOutcome) {
    List<String> outcomeEvents = new ArrayList<>();

    for (FulfillmentRequest fulfillmentRequest : householdOutcome.getFulfillmentRequests()) {
      String event;
      if (isQuestionnaireLinked(fulfillmentRequest)){
        event = createQuestionnaireLinkedEvent(householdOutcome);
        outcomeEvents.add(event);
      } else {
        switch (householdOutcome.getSecondaryOutcome()) {
        case "Paper Questionnaire required by post":
        case "Paper H Questionnaire required by post":
        case "Paper HC Questionnaire required by post":
        case "Paper I Questionnaire required by post":
          event = createQuestionnaireRequiredByPostEvent(householdOutcome, fulfillmentRequest);
          outcomeEvents.add(event);
          break;
        case "HUAC required by text":
        case "IUAC required by text":
        case "UAC required by text":
          event = createUacRequiredByTextEvent(householdOutcome, fulfillmentRequest);
          outcomeEvents.add(event);
          break;
        default:
          log.error("Failed to find valid Secondary Outcome: {}", householdOutcome.getSecondaryOutcome());
          break;
        }
      }
    }
    return outcomeEvents;
    }

  private String createUacRequiredByTextEvent(HouseholdOutcome householdOutcome, FulfillmentRequest fulfillmentRequest) {
    Product product = getProductFromQuestionnaireType(fulfillmentRequest);
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("productCodeLookup",product.getFulfilmentCode());
    if (product.getCaseType().equals(Product.CaseType.HI)){
      root.put("generateIndividualCaseId", UUID.randomUUID());
      root.put("grabContactNo", fulfillmentRequest.getRequesterPhone());
    }

    return TemplateCreator.createOutcomeMessage("FULFILMENT_REQUESTED",root);
  }

  private String createQuestionnaireRequiredByPostEvent(HouseholdOutcome householdOutcome, FulfillmentRequest fulfillmentRequest) {
    Product product = getProductFromQuestionnaireType(fulfillmentRequest);
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("productCodeLookup",product.getFulfilmentCode());
    if (product.getCaseType().equals(Product.CaseType.HI)){
      root.put("generateIndividualCaseId", UUID.randomUUID());
      root.put("individualContactDetails", fulfillmentRequest.getRequesterForename());
    }

    return TemplateCreator.createOutcomeMessage("FULFILMENT_REQUESTED",root);
  }

  private String createQuestionnaireLinkedEvent(HouseholdOutcome householdOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    return TemplateCreator.createOutcomeMessage("QUESTIONNAIRE_LINKED",root);
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId()!=null);
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
