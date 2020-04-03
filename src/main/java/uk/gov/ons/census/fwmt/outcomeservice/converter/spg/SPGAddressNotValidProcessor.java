package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("ADDRESS_NOT_VALID")
public class SpgAddressNotValidProcessor implements SpgOutcomeServiceProcessor {

  @Autowired
  DateFormat dateFormat;
  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;
  @Autowired
  private GatewayEventManager gatewayEventManager;
  @Autowired
  private SpgReasonCodeLookup spgReasonCodeLookup;

  @Override
  public UUID process(SpgOutcomeSuperSetDto outcome, UUID caseIdHolder) throws GatewayException {
    UUID caseId = (outcome.getCaseId() != null) ? outcome.getCaseId() : caseIdHolder;

    String reasonCode = spgReasonCodeLookup.getLookup(outcome.getOutcomeCode());

    String eventDateTime = dateFormat.format(outcome.getEventDate());
    outcome.getEventDate();
    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", outcome);
    root.put("reason", reasonCode);
    root.put("caseId", caseId);
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, spg);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(outcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    gatewayEventManager
        .triggerEvent(String.valueOf(caseId), CESPG_OUTCOME_SENT,
            "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT,
            "transactionId", outcome.getTransactionId().toString());
    return caseId;
  }
}
