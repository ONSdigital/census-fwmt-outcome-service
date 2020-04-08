package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache.GatewayCacheBuilder;
import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_FULFILMENT_REQUESTED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.FULFILMENT_REQUESTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;
import static uk.gov.ons.ctp.integration.common.product.model.Product.RequestChannel.FIELD;

@Slf4j
@Component("FULFILMENT_REQUESTED")
public class SpgFulfilmentRequestProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  DateFormat dateFormat;
  @Autowired
  private ProductReference productReference;
  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;
  @Autowired
  private GatewayEventManager gatewayEventManager;
  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    UUID caseId = (outcome.getCaseId() != null) ? outcome.getCaseId() : caseIdHolder;
    if (outcome.getFulfilmentRequests() == null) {
      return caseIdHolder;
    }
    for (FulfilmentRequestDto fulfilmentRequest : outcome.getFulfilmentRequests()) {
      if (!isQuestionnaireLinked(fulfilmentRequest)) {

        String eventDateTime = dateFormat.format(outcome.getEventDate());
        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", outcome);
        root.put("caseId", caseId);
        root.put("eventDate", eventDateTime);
        String outcomeEvent = createQuestionnaireRequiredByPostEvent(root, fulfilmentRequest,
            String.valueOf(caseIdHolder));

        gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
            GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
        gatewayEventManager.triggerEvent(String.valueOf(caseIdHolder), CESPG_OUTCOME_SENT,
            "type", CESPG_FULFILMENT_REQUESTED_OUTCOME_SENT,
            "transactionId", outcome.getTransactionId().toString(),
            "routing key", GatewayOutcomeQueueConfig.GATEWAY_FULFILMENT_REQUEST_ROUTING_KEY);
      }
    }
    return caseId;
  }

  private String createQuestionnaireRequiredByPostEvent(Map<String, Object> root,
      FulfilmentRequestDto fulfilmentRequest,
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
  private Product getProductFromQuestionnaireType(FulfilmentRequestDto fulfilmentRequest) {
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

  private boolean isQuestionnaireLinked(FulfilmentRequestDto fulfilmentRequest) {
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
