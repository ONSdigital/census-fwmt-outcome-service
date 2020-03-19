package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.NEW_ADDRESS_REPORTED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDTO;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component("NEW_ADDRESS_REPORTED")
public class SPGNewAddressReported implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;


  @Override
  public UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException {
    UUID newCaseId = UUID.randomUUID();
    boolean isDelivered = isDelivered(outcome);
    cacheData(outcome, newCaseId, isDelivered);

    String eventDateTime = outcome.getEventDate().toString();

    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", outcome);
    root.put("newCaseId", newCaseId);
    root.put("address", outcome.getAddress());
    root.put("officerId", outcome.getOfficerId());
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(NEW_ADDRESS_REPORTED, root, spg);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(outcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(newCaseId), CESPG_OUTCOME_SENT,
        "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
        outcome.getTransactionId().toString());

    return newCaseId;
  }

  private void cacheData(SPGOutcomeSuperSetDTO outcome, UUID newCaseId, boolean isDelivered) throws GatewayException {
    GatewayCache cache = gatewayCacheService.getById(String.valueOf(newCaseId));
    if (cache==null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case does not exist in cache: {}",
          outcome.getCaseId());
    }

    gatewayCacheService.save(cache.toBuilder()
        .caseId(String.valueOf(newCaseId))
        .delivered(isDelivered)
        .existsInFwmt(false)
        .accessInfo(outcome.getAccessInfo())
        .careCodes(outcome.getCareCodes().toString())
        .build());
  }

  private boolean isDelivered(SPGOutcomeSuperSetDTO outcome) {
    List<FulfilmentRequestDTO> fulfilmentRequestList = outcome.getFulfillmentRequests();
    boolean isDelivered = false;

    for (FulfilmentRequestDTO fulfilmentRequest : fulfilmentRequestList) {
      if (fulfilmentRequest.getQuestionnaireID() != null) {
        isDelivered = true;
        break;
      }
    }
    return isDelivered;
  }
}
