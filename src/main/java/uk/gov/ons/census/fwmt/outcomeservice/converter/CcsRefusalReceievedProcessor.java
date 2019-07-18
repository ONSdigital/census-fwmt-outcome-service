package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;

@Component
public class CcsRefusalReceievedProcessor implements CcsOutcomeServiceProcessor {

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Soft refusal", "Hard refusal", "Extraordinary refusal");
    return validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("surveyType", "CCS");
    root.put("report", "not in datamap");
    root.put("agentId", ccsPropertyListingOutcome.getUsername());
    root.put("householdOutcome", ccsPropertyListingOutcome);
    root.put("refusalType", BuildSecondaryOutcomeMaps.secondaryOutcomeMap.get(ccsPropertyListingOutcome.getSecondaryOutcome()));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root);
  }
}
