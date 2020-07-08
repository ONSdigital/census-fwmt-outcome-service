package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.FAILED_TO_LOOKUP_OUTCOME_CODE;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_SPG_OUTCOME;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.PROCESSING_CE_OUTCOME;

@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private Map<String, OutcomeServiceProcessor> outcomeServiceProcessors;

  @Autowired
  private OutcomeLookup outcomeLookup;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void createSpgOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "SPG",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_SPG_OUTCOME,
          "Survey type", "SPG",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "SPG");
    }
  }

  @Override
  public void createCeOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          "Survey type", "CE",
          "Outcome code", outcome.getOutcomeCode(),
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CE_OUTCOME,
          "Survey type", "CE",
          "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
          "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          "Operation", operation,
          "Operation list", Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "CE");
    }
  }
}
