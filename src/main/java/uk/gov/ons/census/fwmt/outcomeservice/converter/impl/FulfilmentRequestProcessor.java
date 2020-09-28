package uk.gov.ons.census.fwmt.outcomeservice.converter.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache.GatewayCacheBuilder;
import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;


import java.text.DateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Slf4j
@Component("FULFILMENT_REQUESTED")
public class FulfilmentRequestProcessor implements OutcomeServiceProcessor {

  public static final String PROCESSING_OUTCOME = "PROCESSING_OUTCOME";

  public static final String OUTCOME_SENT = "OUTCOME_SENT";

  @Autowired
  private DateFormat dateFormat;

  @Autowired
  private ProductReference productReference;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(OutcomeSuperSetDto outcome, UUID caseIdHolder, String type) throws GatewayException {
    gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_OUTCOME,
    "survey type", type,
    "processor", "FULFILMENT_REQUESTED",
    "original caseId", String.valueOf(outcome.getCaseId()),
    "Site Case id", (outcome.getSiteCaseId() != null ? String.valueOf(outcome.getSiteCaseId()) : "N/A"));
 
    if (outcome.getFulfilmentRequests() == null) return caseIdHolder;
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();
    for (FulfilmentRequestDto fulfilmentRequest : outcome.getFulfilmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest) && fulfilmentRequest.getQuestionnaireType() != null) {
        String eventDateTime = dateFormat.format(outcome.getEventDate());
        Map<String, Object> root = new HashMap<>();
        root.put("outcome", outcome);
        root.put("caseId", caseId);
        root.put("eventDate", eventDateTime);
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest, String.valueOf(caseId),
            type);

        gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
            GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
        gatewayEventManager.triggerEvent(String.valueOf(caseIdHolder), OUTCOME_SENT,
            "survey type", type,
            "type", FULFILMENT_REQUESTED.toString(),
            "transactionId", outcome.getTransactionId().toString(),
            "routing key", GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
      }
    }
    return caseId;
  }

  private String createQuestionnaireRequiredByPostEvent(Map<String, Object> root,
    FulfilmentRequestDto fulfilmentRequest, String caseId, String type) throws GatewayException {

    List<Product> productList = getProductFromQuestionnaireType(fulfilmentRequest);
      if (productList.get(0).getIndividual() && type.equals("HH")) {
        String individualCaseId = String.valueOf(UUID.randomUUID());
        root.put("individualCaseId", individualCaseId);
        root.put("surveyType", type);
      }
    root.put("packcode", productList.get(0).getFulfilmentCode());
    root.put("requesterTitle", fulfilmentRequest.getRequesterTitle());
    root.put("requesterForename", fulfilmentRequest.getRequesterForename());
    root.put("requesterSurname", fulfilmentRequest.getRequesterSurname());
    root.put("requesterPhone", fulfilmentRequest.getRequesterPhone());
    cacheData(caseId);

    return TemplateCreator.createOutcomeMessage(FULFILMENT_REQUESTED, root);
  }

  private List<Product> getProductFromQuestionnaireType(FulfilmentRequestDto fulfilmentRequest) {
    Product product = new Product();
    List<Product.RequestChannel> requestChannels = Collections.singletonList(FIELD);

    product.setRequestChannels(requestChannels);
    product.setFulfilmentCode(fulfilmentRequest.getQuestionnaireType());

    List<Product> productList = null;
    try {
      productList = productReference.searchProducts(product);
    } catch (CTPException e) {
      log.error("unable to find valid Products {}", e);
    }
    return productList;
  }

  private boolean isQuestionnaireLinked(FulfilmentRequestDto fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }

  private void cacheData(String caseId) {
    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));
    GatewayCacheBuilder builder ;
    if (cache == null) builder = GatewayCache.builder();
    else builder = cache.toBuilder();

    gatewayCacheService.save(builder
        .caseId(caseId).delivered(true)
        .type(0)
        .build());
  }
}