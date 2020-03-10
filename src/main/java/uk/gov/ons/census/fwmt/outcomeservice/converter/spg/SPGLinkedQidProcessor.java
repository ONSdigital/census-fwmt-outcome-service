package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_IS_NULL;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.LINKED_QID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("LINKED_QID")
public class SPGLinkedQidProcessor implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void processMessage(SPGOutcome spgOutcome) throws GatewayException {
    if (spgOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), null, "Fulfilment Request is null", spgOutcome.getCaseReference(),
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", spgOutcome.getPrimaryOutcomeDescription(), "Secondary Outcome",
              spgOutcome.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : spgOutcome.getFulfillmentRequests()) {
      if (isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = spgOutcome.getEventDate().toString();

        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", spgOutcome);
        root.put("questionnaireId", fulfilmentRequest.getQuestionnaireID());
        root.put("eventDate", eventDateTime + "Z");

        String outcomeEvent = TemplateCreator.createOutcomeMessage(LINKED_QID, root, spg);

        gatewayOutcomeProducer.sendQuestionnaireLinked(outcomeEvent, String.valueOf(spgOutcome.getTransactionId()));
        // TODO : what to set as case id?
        gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getSiteCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId", spgOutcome.getTransactionId().toString(),
            "Case Ref", spgOutcome.getCaseReference());
      }
    }
  }

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }
}
