package uk.gov.ons.census.fwmt.outcomeservice.converter.household;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.GatewayOutcomeQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.converter.HhOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.HH_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.CONTACT_MADE;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.PrimaryOutcomes.NON_VALID_HOUSEHOLD;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.household;

@Component
public class HhAddressNotValidProcessor implements HhOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(HouseholdOutcome householdOutcome) {
    return isNonValidHousehold(householdOutcome) || isSplitAddress(householdOutcome);
  }

  @Override
  public void processMessage(HouseholdOutcome householdOutcome) throws GatewayException {
    String eventDateTime = householdOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("secondaryOutcome",
        HhSecondaryOutcomeMap.householdSecondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, household);

    gatewayOutcomeProducer.sendOutcome(outcomeEvent, String.valueOf(householdOutcome.getTransactionId()),
        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY);
    gatewayEventManager.triggerEvent(String.valueOf(householdOutcome.getCaseId()), HH_OUTCOME_SENT, "type",
        "HH_ADDRESS_NOT_VALID_OUTCOME_SENT", "transactionId", householdOutcome.getTransactionId().toString(),
        "Case Ref", householdOutcome.getCaseReference());
  }

  private boolean isNonValidHousehold(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Derelict", "Demolished", "Cant find", "Unaddressable Object", "Non-res", "Duplicate", "Under Const",
            "CE - No contact", "CE - Contact made");
    return householdOutcome.getPrimaryOutcome().equals(NON_VALID_HOUSEHOLD.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }

  private boolean isSplitAddress(HouseholdOutcome householdOutcome) {
    List<String> validSecondaryOutcomes = Collections.singletonList("Split address");
    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validSecondaryOutcomes
        .contains(householdOutcome.getSecondaryOutcome());
  }
}
