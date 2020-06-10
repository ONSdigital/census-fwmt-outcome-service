package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_TYPE_CHANGED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;
import static uk.gov.ons.census.fwmt.outcomeservice.util.SpgUtilityMethods.regionLookup;

@Component("ADDRESS_TYPE_CHANGED_CE_EST")
public class SpgAddressTypeChangedCeEstProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  private DateFormat dateFormat;

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    Map<String, Object> root = new HashMap<>();
    UUID caseId = (caseIdHolder != null) ? caseIdHolder : outcome.getCaseId();
    root.put("caseId", caseId);
    UUID newCaseId = UUID.randomUUID();
    root.put("newCaseId", newCaseId);
    cacheData(outcome, caseId, newCaseId);

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    root.put("spgOutcome", outcome);
    root.put("region", regionLookup(outcome.getOfficerId()));
    root.put("eventDate", eventDateTime);
    root.put("estabType", "CE");

    if (outcome.getCeDetails() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", outcome.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED, root, spg);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(caseId), CESPG_OUTCOME_SENT,
        "type", CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT,
        "transactionId", outcome.getTransactionId().toString(),
        "routing key", GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);

    return newCaseId;
  }

  private void cacheData(SpgOutcomeSuperSetDto outcome, UUID caseId, UUID newCaseId) throws GatewayException {
    GatewayCache parentCacheJob = gatewayCacheService.getById(caseId.toString());
    if (parentCacheJob == null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Parent case does not exist in cache: {}", caseId);
    }

    GatewayCache newCachedJob = gatewayCacheService.getById(newCaseId.toString());
    if (newCachedJob != null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "New case exists in cache: {}", caseId);
    }

    gatewayCacheService.save(GatewayCache.builder()
        .caseId(newCaseId.toString())
        .existsInFwmt(false)
        .accessInfo(outcome.getAccessInfo())
        .careCodes(SpgOutcomeSuperSetDto.careCodesToText(outcome.getCareCodes()))
        .build());
  }
}

