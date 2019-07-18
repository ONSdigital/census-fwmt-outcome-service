package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;

@Component
public class CssContactNotNeeded implements CcsOutcomeServiceProcessor {

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> validSecondaryOutcomes = Collections.singletonList("Soft refusal");
    return validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", ccsPropertyListingOutcome);

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root);
  }
}
