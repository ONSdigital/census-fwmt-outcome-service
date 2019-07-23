package uk.gov.ons.census.fwmt.outcomeservice.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROPERTY_LISTING_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.ADDRESS_NOT_VALID;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.ccs;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressLevel;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressType;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getOrganisationName;

@Component
public class CssAddressNotValidProcessor implements CcsOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    return isNonValidCcsPropertyListing(ccsPropertyListingOutcome);
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", ccsPropertyListingOutcome);
    root.put("addressType", getAddressType(ccsPropertyListingOutcome));
    root.put("addressLevel", getAddressLevel(ccsPropertyListingOutcome));
    root.put("organisationName", getOrganisationName(ccsPropertyListingOutcome));
    root.put("secondaryOutcome",
        BuildSecondaryOutcomeMaps.secondaryOutcomeMap.get(ccsPropertyListingOutcome.getSecondaryOutcome()));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, ccs);

    try {

      gatewayOutcomeProducer
          .sendPropertyListing(outcomeEvent, String.valueOf(ccsPropertyListingOutcome.getTransactionId()));
      gatewayEventManager
          .triggerEvent(String.valueOf(ccsPropertyListingOutcome.getPropertyListingCaseId()), PROPERTY_LISTING_SENT,
              LocalTime.now());
    } catch (GatewayException e) {
      e.printStackTrace();
    }
  }

  private boolean isNonValidCcsPropertyListing(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Derelict /Uninhabitable", "Under construction", "Potential Residential");
    return validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }

  // TODO generic example

  //  public <T> boolean isValid(T outcome) {
  //    Class input = outcome.getClass();
  //
  //    if(input == HouseholdOutcome.class) {
  //      HouseholdOutcome householdOutcome = (HouseholdOutcome) outcome;
  //      return isNonValidHousehold(householdOutcome) || isSplitAddress(householdOutcome);
  //    } else if(input == CCSPropertyListingOutcome.class) {
  //      CCSPropertyListingOutcome ccsPropertyListingOutcome = (CCSPropertyListingOutcome) outcome;
  //      return isNonValidCcsPropertyListing(ccsPropertyListingOutcome);
  //    }
  //    return false;
  //  }
  //
  //
  //  public <T> void processMessage(T outcome) {
  //
  //  }

  //  private boolean isNonValidHousehold(HouseholdOutcome householdOutcome) {
  //    List<String> validSecondaryOutcomes = Arrays
  //        .asList("Derelict", "Demolished", "Cant find", "Unaddressable Object", "Non-res", "Duplicate", "Under Const");
  //    return householdOutcome.getPrimaryOutcome().equals(NON_VALID_HOUSEHOLD.toString()) && validSecondaryOutcomes
  //        .contains(householdOutcome.getSecondaryOutcome());
  //  }
  //
  //  private boolean isSplitAddress(HouseholdOutcome householdOutcome) {
  //    List<String> validSecondaryOutcomes = Collections.singletonList("Split address");
  //    return householdOutcome.getPrimaryOutcome().equals(CONTACT_MADE.toString()) && validSecondaryOutcomes
  //        .contains(householdOutcome.getSecondaryOutcome());
  //  }
}
