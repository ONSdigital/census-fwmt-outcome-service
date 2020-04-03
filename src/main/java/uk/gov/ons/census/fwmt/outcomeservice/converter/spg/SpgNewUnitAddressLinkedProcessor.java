package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.NEW_UNIT_ADDRESS;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("NEW_UNIT_ADDRESS")
public class SpgNewUnitAddressLinkedProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  DateFormat dateFormat;
  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;
  @Autowired
  private GatewayEventManager gatewayEventManager;
  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    UUID newCaseId = UUID.randomUUID();
    boolean isDelivered = isDelivered(outcome);
    cacheData(outcome, newCaseId, isDelivered);

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", outcome);
    root.put("newUnitCaseId", newCaseId);
    root.put("officerId", outcome.getOfficerId());
    root.put("address", outcome.getAddress());
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(NEW_UNIT_ADDRESS, root, spg);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_CCS_PROPERTYLISTING_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(newCaseId), CESPG_OUTCOME_SENT,
        "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
        outcome.getTransactionId().toString());

    return newCaseId;
  }

  private boolean isDelivered(SpgOutcomeSuperSetDto outcome) {
    List<FulfilmentRequestDto> fulfilmentRequestList = outcome.getFulfillmentRequests();
    if (fulfilmentRequestList == null)
      return false;
    boolean isDelivered = false;
    for (FulfilmentRequestDto fulfilmentRequest : fulfilmentRequestList) {
      if (fulfilmentRequest.getQuestionnaireID() != null) {
        isDelivered = true;
        break;
      }
    }
    return isDelivered;
  }

  private void cacheData(SpgOutcomeSuperSetDto outcome, UUID caseId, boolean isDelivered) throws GatewayException {
    GatewayCache cache = gatewayCacheService.getById(String.valueOf(caseId));
    if (cache != null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case alreadyexist in cache: {}",
          caseId);
    }

    gatewayCacheService.save(GatewayCache.builder()
        .caseId(String.valueOf(caseId))
        .delivered(isDelivered)
        .existsInFwmt(false)
        .accessInfo(outcome.getAccessInfo())
        .careCodes(outcome.getCareCodes().toString())
        .build());
  }
}
