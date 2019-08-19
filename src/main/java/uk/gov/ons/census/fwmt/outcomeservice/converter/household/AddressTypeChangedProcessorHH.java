package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_TYPE_CHANGED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NON_VALID_HOUSEHOLD;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

import java.util.Arrays;
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
public class AddressTypeChangedProcessorHH implements HHOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("CE - No contact", "CE - Contact made");
    return householdOutcome.getPrimaryOutcome().equals(NON_VALID_HOUSEHOLD.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException{
    Map<String, Object> root = new HashMap<>();
    String eventDateTime = householdOutcome.getEventDate().toString();
    root.put("householdOutcome", householdOutcome);
    root.put("estabType", "CE");
    root.put("eventDate", eventDateTime + "Z");
    root.put("secondaryOutcome", householdOutcome.getSecondaryOutcome());

    if (householdOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", householdOutcome.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED, root, household);

    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, new HashMap<>( Map.of("type", "HH_ADDRESS_TYPE_CHANGED_OUTCOME_SENT")));
  }
}
