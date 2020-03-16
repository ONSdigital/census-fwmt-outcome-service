package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("ADDRESS_NOT_VALID")
public class SPGAddressNotValidProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override public void processMessageSpgOutcome(SPGOutcome spgOutcome) throws GatewayException {
    String reasonCode = SPGReasonCodeLookup.getLookup(spgOutcome.getOutcomeCode());

    String eventDateTime = spgOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", spgOutcome);
    root.put("generateCaseId", spgOutcome.getCaseId());
    root.put("secondaryOutcome", reasonCode);
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
    gatewayEventManager
        .triggerEvent(String.valueOf(spgOutcome.getCaseId()), CESPG_OUTCOME_SENT,
            "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT,
            "transactionId", spgOutcome.getTransactionId().toString());
  }

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    String reasonCode = SPGReasonCodeLookup.getLookup(newUnitAddress.getOutcomeCode());
    String newCaseId = String.valueOf(UUID.randomUUID());

    String eventDateTime = newUnitAddress.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", newUnitAddress);
    root.put("generateCaseId", newCaseId);
    root.put("secondaryOutcome", reasonCode);
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(newUnitAddress.getTransactionId()));
    gatewayEventManager
        .triggerEvent(newCaseId, CESPG_OUTCOME_SENT, "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT,
            "transactionId", newUnitAddress.getTransactionId().toString());
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress)
      throws GatewayException {
    String reasonCode = SPGReasonCodeLookup.getLookup(newStandaloneAddress.getOutcomeCode());
    String newCaseId = String.valueOf(UUID.randomUUID());

    String eventDateTime = newStandaloneAddress.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", newStandaloneAddress);
    root.put("generateCaseId", newCaseId);
    root.put("secondaryOutcome", reasonCode);
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, spg);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(newStandaloneAddress.getTransactionId()));
    gatewayEventManager
        .triggerEvent(newCaseId, CESPG_OUTCOME_SENT, "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT,
            "transactionId", newStandaloneAddress.getTransactionId().toString());
  }
}
