package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.util.HashMap;
import java.util.Map;

@Component
public class OutcomeEventFactory {

  static Map<String, String> secondaryOutcomeMap = new HashMap<>();

  @Autowired
  public OutcomeEventFactory(BuildSecondaryOutcomeMaps buildSecondaryOutcomeMaps) {
    buildSecondaryOutcomeMaps.buildSecondaryOutcomeMap();
  }

  public String createOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    String outcomeEvent;
    switch (householdOutcome.getPrimaryOutcome()) {
    case "No Valid Household":
      outcomeEvent = buildAddressNotValidOutcome(householdOutcome);
      break;
    case "Contact Made":
      outcomeEvent = getContactMadeSecondaryOutcome(householdOutcome);
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Primary' outcome found: " + householdOutcome.getPrimaryOutcome());
    }
    return outcomeEvent;
  }

  private String getContactMadeSecondaryOutcome(HouseholdOutcome householdOutcome) throws GatewayException {
    String outcomeEvent;
    switch (householdOutcome.getSecondaryOutcome()) {
    case "Split Address":
      outcomeEvent = buildAddressNotValidOutcome(householdOutcome);
      break;
    case "Hard Refusal":
    case "Extraordinary Refusal":
      outcomeEvent = buildRefusalOutcome(householdOutcome);
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Secondary' outcome found: " + householdOutcome.getSecondaryOutcome());
    }
    return outcomeEvent;
  }

  private String buildAddressNotValidOutcome(HouseholdOutcome householdOutcome) {
    String outcomeEvent;

    if (householdOutcome.getSecondaryOutcome().contains("Property is a CE")) {
      outcomeEvent = buildCEOutcome(householdOutcome);
    } else {
      Map<String, Object> root = new HashMap<>();
      root.put("householdOutcome", householdOutcome);
      root.put("secondaryOutcome", secondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));

      outcomeEvent = TemplateCreator.createOutcomeMessage("ADDRESS_NOT_VALID", root);
    }
    return outcomeEvent;
  }

  private String buildRefusalOutcome(HouseholdOutcome householdOutcome) {
    String outcomeEvent;
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("refusalType", secondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));

    outcomeEvent = TemplateCreator.createOutcomeMessage("REFUSAL_RECEIVED", root);
    return outcomeEvent;
  }

  private String buildCEOutcome(HouseholdOutcome householdOutcome) {
    String outcomeEvent;
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("estabType", "CE");

    if (householdOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualSuspects", 0);
    } else {
      root.put("usualSuspects", householdOutcome.getCeDetails().getUsualResidents());
    }

    outcomeEvent = TemplateCreator.createOutcomeMessage("ADDRESS_TYPE_CHANGED", root);
    return outcomeEvent;
  }
}
