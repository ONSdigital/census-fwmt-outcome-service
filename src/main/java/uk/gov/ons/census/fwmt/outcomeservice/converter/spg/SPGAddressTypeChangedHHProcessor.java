package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_TYPE_CHANGED_HH;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SPGOutcomeSuperSetDTO;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component("ADDRESS_TYPE_CHANGED_HH")
public class SPGAddressTypeChangedHHProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public UUID process(SPGOutcomeSuperSetDTO outcome, UUID caseIdHolder) throws GatewayException {
    cacheData(outcome);

    Map<String, Object> root = new HashMap<>();
    String eventDateTime = outcome.getEventDate().toString();
    root.put("spgOutcome", outcome);
    root.put("caseId", outcome.getCaseId());
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED_HH, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(outcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), CESPG_OUTCOME_SENT, "type",
        CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT, "transactionId", outcome.getTransactionId().toString());

    return outcome.getCaseId();
  }

  private void cacheData(SPGOutcomeSuperSetDTO outcome) throws GatewayException {
    GatewayCache cachedItem = gatewayCacheService.getById(String.valueOf(outcome.getCaseId()));
    if (cachedItem==null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case does not exist in cache: {}",
          outcome.getCaseId());
    }

    gatewayCacheService.save(cachedItem.toBuilder()
            .accessInfo(outcome.getAccessInfo())
            .careCodes(outcome.getCareCodes().toString())
            .build());
  }
}
