package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
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
  public void processMessageSpgOutcome(SPGOutcome spgOutcome) throws GatewayException {
    // TODO : depending on the outcome code, caseId 'might' be provided or will be the NEW caseId allocated to a new Address must be used
    if (spgOutcome.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
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
        gatewayEventManager.triggerEvent(String.valueOf(spgOutcome.getCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId", spgOutcome.getTransactionId().toString());
      }
    }
  }

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    // TODO : depending on the outcome code, caseId 'might' be provided or will be the NEW caseId allocated to a new Address must be used
    if (newUnitAddress.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", newUnitAddress.getPrimaryOutcomeDescription(), "Secondary Outcome",
              newUnitAddress.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : newUnitAddress.getFulfillmentRequests()) {
      if (isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = newUnitAddress.getEventDate().toString();

        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", newUnitAddress);
        root.put("questionnaireId", fulfilmentRequest.getQuestionnaireID());
        root.put("eventDate", eventDateTime + "Z");

        String outcomeEvent = TemplateCreator.createOutcomeMessage(LINKED_QID, root, spg);

        gatewayOutcomeProducer.sendQuestionnaireLinked(outcomeEvent, String.valueOf(newUnitAddress.getTransactionId()));
        gatewayEventManager.triggerEvent(String.valueOf(newUnitAddress.getCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId", newUnitAddress.getTransactionId().toString());
      }
    }
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress)
      throws GatewayException {
    // TODO : depending on the outcome code, caseId 'might' be provided or will be the NEW caseId allocated to a new Address must be used
    if (newStandaloneAddress.getFulfillmentRequests() == null) {
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), (Exception) null, "Fulfilment Request is null",
              FAILED_FULFILMENT_REQUEST_IS_NULL,
              "Primary Outcome", newStandaloneAddress.getPrimaryOutcomeDescription(), "Secondary Outcome",
              newStandaloneAddress.getSecondaryOutcomeDescription());
      return;
    }
    for (FulfilmentRequest fulfilmentRequest : newStandaloneAddress.getFulfillmentRequests()) {
      if (isQuestionnaireLinked(fulfilmentRequest)) {
        String eventDateTime = newStandaloneAddress.getEventDate().toString();

        Map<String, Object> root = new HashMap<>();
        root.put("spgOutcome", newStandaloneAddress);
        root.put("questionnaireId", fulfilmentRequest.getQuestionnaireID());
        root.put("eventDate", eventDateTime + "Z");

        String outcomeEvent = TemplateCreator.createOutcomeMessage(LINKED_QID, root, spg);

        gatewayOutcomeProducer
            .sendQuestionnaireLinked(outcomeEvent, String.valueOf(newStandaloneAddress.getTransactionId()));
        gatewayEventManager.triggerEvent(String.valueOf(newStandaloneAddress.getCaseId()), CESPG_OUTCOME_SENT, "type",
            CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId", newStandaloneAddress.getTransactionId().toString());
      }
    }
  }

  private boolean isQuestionnaireLinked(FulfilmentRequest fulfilmentRequest) {
    return (fulfilmentRequest.getQuestionnaireID() != null);
  }
}
