package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

  @Override
  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    String generatedCaseId = String.valueOf(UUID.randomUUID());
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = spgOutcome.getEventDate().toString();
    root.put("spgOutcome", spgOutcome);
    root.put("generatedCaseId", generatedCaseId);
    root.put("eventDate", eventDateTime + "Z");

    if (spgOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", spgOutcome.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED_CEEST, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(generatedCaseId, CESPG_OUTCOME_SENT, "type",
        CESPG_ADDRESS_TYPE_CHANGED_OUTCOME_SENT, "transactionId", spgOutcome.getTransactionId().toString(),
        "Case Ref", spgOutcome.getCaseReference());
  }
}

