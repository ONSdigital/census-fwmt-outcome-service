package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_TYPE_CHANGED_CEEST;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("ADDRESS_TYPE_CHANGED_CEEST")
public class SPGAddressTypeChangedCEESTProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public void processMessageSpgOutcome(SPGOutcome spgOutcome) throws GatewayException {
    if (gatewayCacheService.getById(String.valueOf(spgOutcome.getCaseId())) == null) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Case does not exist in cache: {}",
          spgOutcome.getCaseId());
    }

    gatewayCacheService.save(GatewayCache.builder()
        .caseId(String.valueOf(spgOutcome.getCaseId()))
        .accessInfo(spgOutcome.getAccessInfo())
        .careCodes(spgOutcome.getCareCodes().toString())
        .managerTitle(spgOutcome.getCeDetails().getManagerTitle())
        .managerFirstname(spgOutcome.getCeDetails().getManagerForename())
        .managerSurname(spgOutcome.getCeDetails().getManagerSurname())
        .contactPhoneNumber(spgOutcome.getCeDetails().getContactPhone())
        .build());

    Map<String, Object> root = new HashMap<>();
    String eventDateTime = spgOutcome.getEventDate().toString();
    root.put("spgOutcome", spgOutcome);
    root.put("generatedCaseId", spgOutcome.getCaseId());
    root.put("eventDate", eventDateTime + "Z");

    if (spgOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", spgOutcome.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED_CEEST, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getCaseId()), CESPG_OUTCOME_SENT, "type",
        CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT, "transactionId", spgOutcome.getTransactionId().toString());
  }

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    // TODO : the case should exist in the Gateway Cache (an error case if not):
    // TODO : cache any Care Codes (CE Details) and Access Information
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = newUnitAddress.getEventDate().toString();
    root.put("spgOutcome", newUnitAddress);
    root.put("generatedCaseId", "caseId");
    root.put("eventDate", eventDateTime + "Z");
    root.put("usualResidents", 0);

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED_CEEST, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(newUnitAddress.getTransactionId()));
    gatewayEventManager.triggerEvent("caseId", CESPG_OUTCOME_SENT, "type",
        CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT, "transactionId", newUnitAddress.getTransactionId().toString());
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress)
      throws GatewayException {
    // TODO : the case should exist in the Gateway Cache (an error case if not):
    // TODO : cache any Care Codes (CE Details) and Access Information
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = newStandaloneAddress.getEventDate().toString();
    root.put("spgOutcome", newStandaloneAddress);
    root.put("generatedCaseId", "caseId");
    root.put("eventDate", eventDateTime + "Z");

    if (newStandaloneAddress.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", newStandaloneAddress.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED_CEEST, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(newStandaloneAddress.getTransactionId()));
    gatewayEventManager.triggerEvent("caseId", CESPG_OUTCOME_SENT, "type",
        CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT, "transactionId", newStandaloneAddress.getTransactionId().toString());
  }
}

