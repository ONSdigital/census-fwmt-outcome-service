package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.SpgOutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SpgOutcomeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.dto.SpgOutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_TO_LOOKUP_OUTCOME_CODE;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_CESPG_OUTCOME;

@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private Map<String, SpgOutcomeServiceProcessor> spgOutcomeServiceProcessors;

  @Autowired
  private SpgOutcomeLookup spgOutcomeLookup;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void createSpgOutcomeEvent(SpgOutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = spgOutcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CESPG_OUTCOME,
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = spgOutcomeServiceProcessors.get(operation).process(outcome, caseIdHolder);
    }
  }

  @Override
  public void createCeOutcomeEvent(SpgOutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = spgOutcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CESPG_OUTCOME,
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = spgOutcomeServiceProcessors.get(operation).process(outcome, caseIdHolder);
    }
  }
}
