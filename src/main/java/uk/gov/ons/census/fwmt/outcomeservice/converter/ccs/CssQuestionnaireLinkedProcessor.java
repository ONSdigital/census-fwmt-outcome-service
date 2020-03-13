package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCS_FAILED_FULFILMENT_REQUEST_INVALID;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_FULFILMENT_REQUEST_ADDITIONAL_QID_IN_PROPERTY_LISTING;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.ccs;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressLevel;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressType;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getOrganisationName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component
public class CssQuestionnaireLinkedProcessor implements CcsOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPLOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Complete on paper (full)", "Complete on paper (partial)");
    return validSecondaryOutcomes.contains(ccsPLOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
    if (isQuestionnaireLinked(ccsPLOutcome.getFulfilmentRequests())) {
      int fulfillmentFound = 0;
      for (FulfillmentRequest fulfillmentRequest: ccsPLOutcome.getFulfilmentRequests()) {
        if(fulfillmentFound == 0) {
          UUID newRandomUUID = UUID.randomUUID();

          String eventDateTime = ccsPLOutcome.getEventDate().toString();
          Map<String, Object> root = new HashMap<>();
          root.put("generatedUuid", newRandomUUID);
          root.put("ccsPropertyListingOutcome", ccsPLOutcome);
          root.put("questionnaireId", fulfillmentRequest.getQuestionnaireID());
          root.put("addressType", getAddressType(ccsPLOutcome));
          root.put("addressLevel", getAddressLevel(ccsPLOutcome));
          root.put("organisationName", getOrganisationName(ccsPLOutcome));
          root.put("eventDate", eventDateTime + "Z");

          String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, ccs);

          gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(ccsPLOutcome.getTransactionId()));
          gatewayEventManager.triggerEvent(String.valueOf(newRandomUUID), CCSPL_OUTCOME_SENT,
              "type", "CCSPL_QUESTIONNAIRE_LINKED_OUTCOME_SENT", "transactionId",
              ccsPLOutcome.getTransactionId().toString());

        } else {
          gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "Invalid number of Fulfillment",
              ccsPLOutcome.getPropertyListingCaseReference(), FAILED_FULFILMENT_REQUEST_ADDITIONAL_QID_IN_PROPERTY_LISTING,
              "Primary Outcome", ccsPLOutcome.getPrimaryOutcome(), "Secondary Outcome", ccsPLOutcome.getSecondaryOutcome(),
              "Questionnaire ID", fulfillmentRequest.getQuestionnaireID());
        }
        fulfillmentFound++;
      }
    } else {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "Questionnaire ID is null",
          ccsPLOutcome.getPropertyListingCaseReference(), CCS_FAILED_FULFILMENT_REQUEST_INVALID,
          "Primary Outcome", ccsPLOutcome.getPrimaryOutcome(), "Secondary Outcome", ccsPLOutcome.getSecondaryOutcome());
    }
  }

  private boolean isQuestionnaireLinked(List<FulfillmentRequest> fulfillmentRequests) {
    return (fulfillmentRequests.size() > 0 && fulfillmentRequests.get(0).getQuestionnaireID() != null);
  }
}
