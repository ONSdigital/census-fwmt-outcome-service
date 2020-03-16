package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_IS_NULL;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Slf4j
@Component("FULFILMENT_REQUESTED")
public class SPGFulfilmentRequestProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private ProductReference productReference;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessageSpgOutcome(SPGOutcome spgOutcome) throws GatewayException {
    if (spgOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
              spgOutcome.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : spgOutcome.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = spgOutcome.getEventDate().toString();
        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", spgOutcome);
        root.put("eventDate", eventDateTime + "Z");
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest,
            String.valueOf(spgOutcome.getCaseId()));

        gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
        gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
            spgOutcome.getTransactionId().toString());
      }
    }
  }

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    if (newUnitAddress.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(),
              "Secondary Outcome", newUnitAddress.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : newUnitAddress.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = newUnitAddress.getEventDate().toString();
        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", newUnitAddress);
        root.put("eventDate", eventDateTime + "Z");
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest, "N/A");

        gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(newUnitAddress.getTransactionId()));
        // TODO : what to set as case id?
        gatewayEventManager.triggerEvent(String.valueOf(newUnitAddress.getCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
            newUnitAddress.getTransactionId().toString());
      }
    }
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress) throws GatewayException {
    if (newStandaloneAddress.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", newStandaloneAddress.getPrimaryOutcomeDescription(), "Secondary Outcome",
              newStandaloneAddress.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : newStandaloneAddress.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = newStandaloneAddress.getEventDate().toString();
        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", newStandaloneAddress);
        root.put("eventDate", eventDateTime + "Z");
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest, "N/A");

        gatewayOutcomeProducer
            .sendFulfilmentRequest(outcomeEvent, String.valueOf(newStandaloneAddress.getTransactionId()));
        gatewayEventManager.triggerEvent(String.valueOf(newStandaloneAddress.getCaseId()), CESPG_OUTCOME_SENT,
            "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT,
            "transactionId", newStandaloneAddress.getTransactionId().toString());
      }
    }
  }

  private String createQuestionnaireRequiredByPostEvent(Map<String, Object> root, FulfilmentRequest fulfilmentRequest,
      String caseId) {
    String newCaseId = String.valueOf(UUID.randomUUID());
    Product product = getProductFromQuestionnaireType(fulfilmentRequest);
    root.put("productCodeLookup", product.getFulfilmentCode());
    root.put("title", fulfilmentRequest.getRequesterTitle());
    root.put("forename", fulfilmentRequest.getRequesterForename());
    root.put("surname", fulfilmentRequest.getRequesterSurname());
    root.put("telNo", fulfilmentRequest.getRequesterPhone());

    if (product.getIndividual()) {
      root.put("householdIndicator", 0);
      root.put("individualCaseId", newCaseId);
    } else {
      root.put("householdIndicator", 1);
      if (caseId.equals("N/A")) {
        root.put("generatedCaseId", newCaseId);
      } else {
        root.put("generatedCaseId", caseId);
      }
    }
    return TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root, spg);
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

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }
}
