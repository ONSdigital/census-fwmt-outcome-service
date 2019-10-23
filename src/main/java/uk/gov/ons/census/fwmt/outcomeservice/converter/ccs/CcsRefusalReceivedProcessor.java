package uk.gov.ons.census.fwmt.outcomeservice.converter.ccs;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.REFUSAL_RECEIVED;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.ccs;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressLevel;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getAddressType;
import static uk.gov.ons.census.fwmt.outcomeservice.util.CcsUtilityMethods.getOrganisationName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.CcsOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

@Component
public class CcsRefusalReceivedProcessor implements CcsOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private JobCacheManager jobCacheManager;

  @Override
  public boolean isValid(CCSPropertyListingOutcome ccsPLOutcome) {
    List<String> validSecondaryOutcomes = Arrays.asList("Soft refusal", "Hard refusal", "Extraordinary refusal");
    return validSecondaryOutcomes.contains(ccsPLOutcome.getSecondaryOutcome());
  }

  @Override
  public void processMessage(CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
    UUID newRandomUUID = UUID.randomUUID();
    if(ccsPLOutcome.getSecondaryOutcome().equals("Soft refusal")) {
      jobCacheManager.cacheCCSOutcome(String.valueOf(newRandomUUID), ccsPLOutcome);
    }

    CcsSecondaryOutcomeMap ccsSecondaryOutcomeMap = new CcsSecondaryOutcomeMap();
    String eventDateTime = ccsPLOutcome.getEventDate().toString();
    Map<String, Object> root = new HashMap<>();
    root.put("generatedUuid", newRandomUUID);
    root.put("ccsPropertyListingOutcome", ccsPLOutcome);
    root.put("addressType", getAddressType(ccsPLOutcome));
    root.put("addressLevel", getAddressLevel(ccsPLOutcome));
    root.put("organisationName", getOrganisationName(ccsPLOutcome));
    root.put("refusalType", ccsSecondaryOutcomeMap.ccsSecondaryOutcomeMap.get(ccsPLOutcome.getSecondaryOutcome()));
    root.put("eventDate", eventDateTime + "Z");

    String outcomeEvent = TemplateCreator.createOutcomeMessage(REFUSAL_RECEIVED, root, ccs);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(ccsPLOutcome.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(ccsPLOutcome.getPropertyListingCaseId()), CCSPL_OUTCOME_SENT,
        "type", "CCSPL_REFUSAL_RECEIVED_OUTCOME_SENT", "transactionId", ccsPLOutcome.getTransactionId().toString());
  }
}
