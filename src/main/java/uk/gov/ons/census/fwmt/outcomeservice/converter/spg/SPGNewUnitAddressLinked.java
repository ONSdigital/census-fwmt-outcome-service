package uk.gov.ons.census.fwmt.outcomeservice.converter.spg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SPGOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CESPG_OUTCOME_SENT;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.EventType.NEW_UNIT_ADDRESS;
import static uk.gov.ons.census.fwmt.outcomeservice.enums.SurveyType.spg;

@Component("NEW_UNIT_ADDRESS")
public class SPGNewUnitAddressLinked implements SPGOutcomeServiceProcessor {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayCacheService gatewayCacheService;

  @Override
  public void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException {
    UUID newRandomUUID = UUID.randomUUID();
    String eventDateTime = newUnitAddress.getEventDate().toString();
    List<FulfilmentRequest> fulfilmentRequestList = newUnitAddress.getFulfillmentRequests();
    boolean isDelivered = false;

    for (FulfilmentRequest fulfilmentRequest : fulfilmentRequestList) {
      if (fulfilmentRequest.getQuestionnaireID() != null) isDelivered = true;
    }

    gatewayCacheService.save(GatewayCache.builder()
        .caseId(String.valueOf(newRandomUUID))
        .delivered(isDelivered)
        .existsInFwmt(false)
        .accessInfo(newUnitAddress.getAccessInfo())
        .careCodes(newUnitAddress.getCareCodes().toString())
        .build());

    Map<String, Object> root = new HashMap<>();
    root.put("spgOutcome", newUnitAddress);
    root.put("generatedUuid", newRandomUUID);
    root.put("eventDate", eventDateTime + "Z");
    root.put("agentId", newUnitAddress.getOfficerId());
    root.put("address", newUnitAddress.getAddress());

    String outcomeEvent = TemplateCreator.createOutcomeMessage(NEW_UNIT_ADDRESS, root, spg);

    gatewayOutcomeProducer.sendPropertyListing(outcomeEvent, String.valueOf(newUnitAddress.getTransactionId()));
    gatewayEventManager.triggerEvent(String.valueOf(newRandomUUID), CESPG_OUTCOME_SENT,
        "type", CESPG_ADDRESS_NOT_VALID_OUTCOME_SENT, "transactionId",
        newUnitAddress.getTransactionId().toString());
  }

  @Override
  public void processMessageSpgOutcome(SPGOutcome spgOutcome) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress) {
    throw new UnsupportedOperationException();
  }
}
