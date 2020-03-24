package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_IS_NULL;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.common.data.household.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HHOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

@Slf4j
@Component
public class FulfilmentRequestProcessorHH implements HHOutcomeServiceProcessor {

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
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException {
    if (householdOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
          householdOutcome.getCaseReference(), FAILED_FULFILMENT_REQUEST_IS_NULL,
          "Primary Outcome", householdOutcome.getPrimaryOutcome(), "Secondary Outcome",
          householdOutcome.getSecondaryOutcome());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : householdOutcome.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {
        createQuestionnaireRequiredByPostEvent(householdOutcome, fulfilmentRequest);
      }
    }
  }

  private void createQuestionnaireRequiredByPostEvent(HouseholdOutcome householdOutcome,
      FulfilmentRequest fulfilmentRequest) throws GatewayException {
    Product product = getProductFromQuestionnaireType(fulfilmentRequest);
    String eventDateTime = householdOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("productCodeLookup", product.getFulfilmentCode());
    root.put("title", fulfilmentRequest.getRequesterTitle());
    root.put("forename", fulfilmentRequest.getRequesterForename());
    root.put("surname", fulfilmentRequest.getRequesterSurname());
    root.put("telNo", fulfilmentRequest.getRequesterPhone());
    root.put("eventDate", eventDateTime + "Z");

    if (product.getIndividual()) {
      root.put("householdIndicator", 0);
      root.put("individualCaseId", generateUUID());
    } else {
      root.put("householdIndicator", 1);

    }
    String outcomeEvent = TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root, household);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()), GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, "type",
        "HH_FULFILMENT_REQUESTED_OUTCOME_SENT", "transactionId",
        householdOutcome.getTransactionId().toString(), "Case Ref", householdOutcome.getCaseReference());
  }

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }

  @Nonnull
  private Product getProductFromQuestionnaireType(FulfilmentRequest fulfilmentRequest) {
    Product product = new Product();
    List<Product.RequestChannel> requestChannels = Collections.singletonList(FIELD);

    product.setRequestChannels(requestChannels);
    product.setFieldQuestionnaireCode(fulfilmentRequest.getQuestionnaireType());

    List<Product> productList = null;
    try {
      productList = productReference.searchProducts(product);
    } catch (CTPException e) {
      log.error("unable to find valid Products {}", e);
    }
    return Objects.requireNonNull(productList).get(0);
  }

  private String generateUUID() {
    String generatedUUID;
    generatedUUID = String.valueOf(UUID.randomUUID());
    return generatedUUID;
  }
}
