package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NON_VALID_HOUSEHOLD;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HHOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component
public class AddressNotValidProcessorHH implements HHOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    return isNonValidHousehold(householdOutcome) || isSplitAddress(householdOutcome);
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException{
    HouseholdSecondaryOutcomeMap householdSecondaryOutcomeMap = new HouseholdSecondaryOutcomeMap();
    String eventDateTime = householdOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("secondaryOutcome", householdSecondaryOutcomeMap.householdSecondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, household);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, new HashMap<>( Map.of("type", "HH_ADDRESS_NOT_VALID_OUTCOME_SENT")));
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
