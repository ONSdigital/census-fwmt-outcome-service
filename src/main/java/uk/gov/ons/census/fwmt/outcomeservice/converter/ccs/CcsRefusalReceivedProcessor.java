package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROPERTY_LISTING_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.ccs;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressLevel;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressType;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getOrganisationName;

@Component
public class CcsRefusalReceivedProcessor implements CcsOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Soft refusal", "Hard refusal", "Extraordinary refusal");
    return validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    CcsSecondaryOutcomeMap ccsSecondaryOutcomeMap = new CcsSecondaryOutcomeMap();
    Map<String, Object> root = new HashMap<>();
    root.put("ccsPropertyListingOutcome", ccsPropertyListingOutcome);
    root.put("addressType", getAddressType(ccsPropertyListingOutcome));
    root.put("addressLevel", getAddressLevel(ccsPropertyListingOutcome));
    root.put("organisationName", getOrganisationName(ccsPropertyListingOutcome));
    root.put("refusalType",
            ccsSecondaryOutcomeMap.ccsSecondaryOutcomeMap.get(ccsPropertyListingOutcome.getSecondaryOutcome()));

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, ccs);

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
}
