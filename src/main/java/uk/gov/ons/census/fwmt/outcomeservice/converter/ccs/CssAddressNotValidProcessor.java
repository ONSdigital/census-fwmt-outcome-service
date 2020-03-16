package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_OUTCOME_SENT;
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
  public boolean isValid(CCSPropertyListingOutcome ccsPLOutcome) {
    return isNonValidCcsPropertyListing(ccsPLOutcome);
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
    UUID newRandomUUID = UUID.randomUUID();

    String eventDateTime = ccsPLOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("generatedUuid", newRandomUUID);
    root.put("ccsPropertyListingOutcome", ccsPLOutcome);
    root.put("addressType", getAddressType(ccsPLOutcome));
    root.put("addressLevel", getAddressLevel(ccsPLOutcome));
    root.put("organisationName", getOrganisationName(ccsPLOutcome));
    root.put("secondaryOutcome", CcsSecondaryOutcomeMap.ccsSecondaryOutcomeMap.get(ccsPLOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(ADDRESS_NOT_VALID, root, ccs);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(ccsPLOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(newRandomUUID), CCSPL_OUTCOME_SENT,
        "type", "CCSPL_ADDRESS_NOT_VALID_OUTCOME_SENT", "transactionId", ccsPLOutcome.getTransactionId().toString());
  }

  private boolean isNonValidCcsPropertyListing(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    List<String> validSecondaryOutcomes = Arrays
        .asList("Derelict / Uninhabitable", "Under construction", "Non residential or business", "CE Out of scope",
            "Demolished");
    return validSecondaryOutcomes.contains(ccsPropertyListingOutcome.getSecondaryOutcome());
  }
}
