package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.HouseholdTemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NON_VALID_HOUSEHOLD;

@Component
public class AddressNotValidProcessor implements OutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    return isNonValidHousehold(householdOutcome) || isSplitAddress(householdOutcome);
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("surveyType", "Household");
    root.put("householdOutcome", householdOutcome);
    root.put("secondaryOutcome",
        BuildSecondaryOutcomeMaps.secondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));

    String outcomeEvent = HouseholdTemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root);

    try {

      gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
      gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
    } catch (GatewayException e) {
      e.printStackTrace();
    }
  }

  private boolean isNonValidHousehold(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Derelict", "Demolished", "Cant find", "Unaddressable Object", "Non-res", "Duplicate", "Under Const");
    return householdOutcome.getPrimaryOutcome().equals(NON_VALID_HOUSEHOLD.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  private boolean isSplitAddress(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Collections.singletonList("Split address");
    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }
}
