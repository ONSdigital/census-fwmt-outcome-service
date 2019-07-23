package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_TYPE_CHANGED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NON_VALID_HOUSEHOLD;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class AddressTypeChangedProcessor implements OutcomeServiceProcessor {

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
  public void processMessage(HouseholdOutcome householdOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("estabType", "CE");
    root.put("secondaryOutcome", householdOutcome.getSecondaryOutcome());

    if (householdOutcome.getCeDetails().getUsualResidents() == null) {
      root.put("usualResidents", 0);
    } else {
      root.put("usualResidents", householdOutcome.getCeDetails().getUsualResidents());
    }

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_TYPE_CHANGED, root, household);

    try {
      gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()));
      gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), OUTCOME_SENT_RM, LocalTime.now());
    } catch (GatewayException e) {
      e.printStackTrace();
    }
  }
}
