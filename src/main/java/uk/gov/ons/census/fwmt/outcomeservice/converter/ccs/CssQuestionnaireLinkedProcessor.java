package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_QUESTIONNAIRE_LINKED_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.ccs;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressLevel;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressType;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getOrganisationName;

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
  public void processMessage(CCSPropertyListingOutcome ccsPLOutcome) {
    if (isQuestionnaireLinked(ccsPLOutcome.getFulfillmentRequest())) {
      Map<String, Object> root = new HashMap<>();

      root.put("ccsPropertyListingOutcome", ccsPLOutcome);
      root.put("questionnaireId", ccsPLOutcome.getFulfillmentRequest().getQuestionnaireId());
      root.put("addressType", getAddressType(ccsPLOutcome));
      root.put("addressLevel", getAddressLevel(ccsPLOutcome));
      root.put("organisationName", getOrganisationName(ccsPLOutcome));

      String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root, ccs);

      gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(ccsPLOutcome.getTransactionId()));
      gatewayEventManager.triggerEvent(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), CCSPL_QUESTIONNAIRE_LINKED_OUTCOME_SENT);
    }
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId() != null);
  }
}
