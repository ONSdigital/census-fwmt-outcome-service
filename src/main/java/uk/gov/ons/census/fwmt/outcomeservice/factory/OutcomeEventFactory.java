package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
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
      if( householdOutcome.getSecondaryOutcome().equals("Split address")) {
        outcomeEvent = buildAddressNotValidOutcome(householdOutcome);
      } else {
        outcomeEvent = buildRefusalOutcome(householdOutcome);
      }
      break;
    default:
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
          "No valid 'Primary' outcome found: " + householdOutcome.getPrimaryOutcome());
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

  private String buildAddressNotValidOutcome(HouseholdOutcome householdOutcome) {
    String outcomeEvent;
    Map<String, Object> root = new HashMap<>();
    root.put("householdOutcome", householdOutcome);
    root.put("secondaryOutcome", secondaryOutcomeMap.get(householdOutcome.getSecondaryOutcome()));

    outcomeEvent = TemplateCreator.createOutcomeMessage("ADDRESS_NOT_VALID", root);
    return outcomeEvent;
  }

//  private void getSecondaryNoValidHouseholdOutcome(HouseholdOutcome householdOutcome) throws GatewayException {
//    switch (householdOutcome.getSecondaryOutcome()) {
//
//    case "Property is a CE - no contact made":
//      setCEOutcomeEvent(householdOutcome);
//      break;
//    case "Property is a CE - Contact made":
//      setCEOutcomeEvent(householdOutcome);
//      setCEOutcomeContactMadeEvent(householdOutcome);
//      break;
//    default:
//      throw new GatewayException(GatewayException.Fault.BAD_REQUEST,
//          "No valid 'Non Valid Household' secondary outcome found: " + householdOutcome.getSecondaryOutcome());
//    }
//  }

  private void setCEOutcomeEvent(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent) {
    outcomeEvent.getEvent().setType("ADDRESS_TYPE_CHANGED");
    outcomeEvent.getPayload().getCollectionCase().setId(householdOutcome.getCaseId());
    outcomeEvent.getPayload().getCollectionCase().getAddress().setAddressType("CE");
    outcomeEvent.getPayload().getCollectionCase().getAddress()
        .setEstabType(householdOutcome.getCeDetails().getEstablishmentType());
    outcomeEvent.getPayload().getCollectionCase().getAddress()
        .setOrgName(householdOutcome.getCeDetails().getEstablishmentName());
  }

  private void setCEOutcomeContactMadeEvent(HouseholdOutcome householdOutcome, OutcomeEvent outcomeEvent) {
    if (householdOutcome.getCeDetails().getUsualResidents() > 0) {
      outcomeEvent.getPayload().getCollectionCase().setCeExpectedResponses(householdOutcome.getCeDetails().getUsualResidents());
    }
    outcomeEvent.getPayload().getContact().setTitle(householdOutcome.getCeDetails().getManagerTitle());
    outcomeEvent.getPayload().getContact().setForename(householdOutcome.getCeDetails().getManagerForename());
    outcomeEvent.getPayload().getContact().setSurname(householdOutcome.getCeDetails().getManagerSurname());
    outcomeEvent.getPayload().getContact().setTelNo(householdOutcome.getCeDetails().getContactPhone());
  }
}
