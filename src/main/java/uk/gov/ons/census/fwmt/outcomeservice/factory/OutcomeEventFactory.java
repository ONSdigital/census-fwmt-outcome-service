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
    case "Non-valid household":
      outcomeEvent = buildAddressNotValidOutcome(householdOutcome);
      break;
    case "Contact made":
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
    case "Split address":
      outcomeEvent = buildAddressNotValidOutcome(householdOutcome);
      break;
    case "Hard refusal":
    case "Extraordinary refusal":
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

    if (householdOutcome.getSecondaryOutcome().startsWith("CE -")) {
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
    root.put("secondaryOutcome", householdOutcome.getSecondaryOutcome());

    root.put("managerTitle", householdOutcome.getCeDetails().getManagerTitle());

    if (householdOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", householdOutcome.getCeDetails().getUsualResidents());
    }

    outcomeEvent = TemplateCreator.createOutcomeMessage("ADDRESS_TYPE_CHANGED", root);
    return outcomeEvent;
  }
}
