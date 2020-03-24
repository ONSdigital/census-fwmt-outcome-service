package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

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
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache.GatewayCacheBuilder;
import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDTO;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

@Slf4j
@Component("FULFILMENT_REQUESTED")
public class SPGFulfilmentRequestProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private ProductReference productReference;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException {
    UUID caseId = (outcome.getCaseId()!=null)?outcome.getCaseId():caseIdHolder;
    if (outcome.getFulfillmentRequests() == null) {
      return caseIdHolder;
    }
    for (FulfilmentRequestDTO fulfilmentRequest : outcome.getFulfillmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {

        String eventDateTime = outcome.getEventDate().toString();
        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", outcome);
        root.put("caseId", caseId);
        root.put("eventDate", eventDateTime + "Z");
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest,
            String.valueOf(caseIdHolder));

        gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()), GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
        gatewayEventManager.triggerEvent(String.valueOf(caseIdHolder), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
            outcome.getTransactionId().toString());
      }
    }
    return caseId;
  }

  private String createQuestionnaireRequiredByPostEvent(Map<String, Object> root, FulfilmentRequestDTO fulfilmentRequest,
      String caseId) {
    Product product = getProductFromQuestionnaireType(fulfilmentRequest);
    root.put("packcode", product.getFulfilmentCode());
    root.put("requesterTitle", fulfilmentRequest.getRequesterTitle());
    root.put("requesterForename", fulfilmentRequest.getRequesterForename());
    root.put("requesterSurname", fulfilmentRequest.getRequesterSurname());
    root.put("requesterPhone", fulfilmentRequest.getRequesterPhone());
    root.put("caseId", caseId);
    cacheData(caseId);

    if (product.getIndividual()) {
      String individualCaseId = String.valueOf(UUID.randomUUID());
      root.put("householdIndicator", 1);
      root.put("individualCaseId", individualCaseId);
    } else {
      root.put("householdIndicator", 0);
    }
    return TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root, spg);
  }

  @Nonnull
  private Product getProductFromQuestionnaireType(FulfilmentRequestDTO fulfilmentRequest) {
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

  private boolean isQuestionnaireLinked(FulfilmentRequestDTO fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireType() == null);
  }

  private void cacheData(String caseId) {
    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));
    GatewayCacheBuilder builder = null;
    if (cache == null)
      builder = GatewayCache.builder();
    else
      builder = cache.toBuilder();

    gatewayCacheService.save(builder
        .caseId(caseId).delivered(true).build());
  }

}
