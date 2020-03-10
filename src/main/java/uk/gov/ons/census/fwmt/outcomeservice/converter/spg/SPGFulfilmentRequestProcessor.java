package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
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

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_IS_NULL;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;
import static uk.gov.ons.ctp.integration.common.product.model.Product.CaseType.HI;
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
  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    if (spgOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), null, "Fulfilment Request is null", spgOutcome.getCaseReference(),
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
              spgOutcome.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : spgOutcome.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {
        createQuestionnaireRequiredByPostEvent(spgOutcome, fulfilmentRequest);
      }
    }
  }

  private void createQuestionnaireRequiredByPostEvent(SPGOutcome spgOutcome,
      FulfilmentRequest fulfilmentRequest) throws GatewayException {
    String newCaseId = String.valueOf(UUID.randomUUID());
    Product product = getProductFromQuestionnaireType(fulfilmentRequest);
    String eventDateTime = spgOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", spgOutcome);
    root.put("productCodeLookup", product.getFulfilmentCode());
    root.put("title", fulfilmentRequest.getRequesterTitle());
    root.put("forename", fulfilmentRequest.getRequesterForename());
    root.put("surname", fulfilmentRequest.getRequesterSurname());
    root.put("telNo", fulfilmentRequest.getRequesterPhone());
    root.put("eventDate", eventDateTime + "Z");

    if (product.getCaseType().equals(HI)) {
      root.put("householdIndicator", 0);
      root.put("individualCaseId", newCaseId);
    } else {
      root.put("householdIndicator", 1);

    }
    String outcomeEvent = TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root, spg);

    gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    // TODO : what to set as case id?
    gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getSiteCaseId()), CESPG_OUTCOME_SENT, "type",
        "SPG_FULFILMENT_REQUESTED_OUTCOME_SENT", "transactionId",
        spgOutcome.getTransactionId().toString(), "Case Ref", spgOutcome.getCaseReference());
  }

  // TODO : Implement new product lookup lib
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
