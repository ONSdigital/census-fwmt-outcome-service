package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;

import uk.gov.ons.census.fwmt.common.data.ccs.FulfillmentRequest;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.QUESTIONNAIRE_LINKED;

@Component
public class CssQuestionnaireLinkedProcessor implements CcsOutcomeServiceProcessor{
  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> invalidSecondaryOutcomes = Arrays
        .asList("Soft refusal", "Hard refusal", "Extraordinary refusal", "Contact not needed",
            "Derelict /Uninhabitable", "Under construction", "Potential Residential");
    List<String> validSecondaryOutcomes = Arrays
        .asList("Complete on paper in (full)", "Complete on paper (partial)");
    return !invalidSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome()) &&
        validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    if (isQuestionnaireLinked(ccsPropertyListingOutcome.getFulfillmentRequest())) {
      Map<String, Object> root = new HashMap<>();
      root.put("surveyType", "CCS");
      root.put("questionnaireCompleted", completedQuestionnaire(ccsPropertyListingOutcome));
      root.put("agentId", ccsPropertyListingOutcome.getUsername());
      root.put("householdOutcome", ccsPropertyListingOutcome);
      root.put("questionnaireId", ccsPropertyListingOutcome.getFulfillmentRequest().getQuestionnaireId());

      String outcomeEvent = TemplateCreator.createOutcomeMessage(QUESTIONNAIRE_LINKED, root);
    }
  }

  private boolean isQuestionnaireLinked(FulfillmentRequest fulfillmentRequest) {
    return (fulfillmentRequest.getQuestionnaireId() != null);
  }

  private String completedQuestionnaire(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    if(ccsPropertyListingOutcome.getSecondaryOutcome().contains("full")){
      return "full";

    } else if(ccsPropertyListingOutcome.getSecondaryOutcome().contains("partial")){
      return "partial";

    }
    return null;
  }
}
