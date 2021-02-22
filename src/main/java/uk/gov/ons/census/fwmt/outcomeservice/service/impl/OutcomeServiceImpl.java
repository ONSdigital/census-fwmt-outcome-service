package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.RequiredArgsConstructor;
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

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutcomeServiceImpl implements OutcomeService {

  public static final String PROCESSING_HH_OUTCOME = "PROCESSING_HH_OUTCOME";

  public static final String PROCESSING_SPG_OUTCOME = "PROCESSING_SPG_OUTCOME";

  public static final String PROCESSING_CE_OUTCOME = "PROCESSING_CE_OUTCOME";

  public static final String PROCESSING_CCS_PL_OUTCOME = "PROCESSING_CCS_PL_OUTCOME";

  public static final String PROCESSING_CCS_INT_OUTCOME = "PROCESSING_CCS_INT_OUTCOME";

  public static final String PROCESSING_NC_OUTCOME = "PROCESSING_NC_OUTCOME";

  public static final String FAILED_TO_LOOKUP_OUTCOME_CODE = "FAILED_TO_LOOKUP_OUTCOME_CODE";

  private final Map<String, OutcomeServiceProcessor> outcomeServiceProcessors;

  private final OutcomeLookup outcomeLookup;

  private final GatewayEventManager gatewayEventManager;

  @Override
  @Transactional
  public void createSpgOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "SPG";
    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_SPG_OUTCOME,
            "Survey type", "SPG",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, surveyType);
      }
    }
  }

  private void triggerError(OutcomeSuperSetDto outcome, String surveyType) {
    gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, "No outcome code found",
        String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
        "Survey type", surveyType,
        "Outcome code", outcome.getOutcomeCode(),
        "Secondary Outcome", outcome.getSecondaryOutcomeDescription());
  }

  @Override
  @Transactional
  public void createCeOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "CE";

    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CE_OUTCOME,
            "Survey type", "CE",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, surveyType);
      }
    }
  }

  @Override
  @Transactional
  public void createHhOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "HH";
    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_HH_OUTCOME,
            "Survey type", "HH",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "HH");
      }
    }
  }

  @Override
  @Transactional
  public void createCcsPropertyListingOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "CCS PL";

    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_PL_OUTCOME,
            "Survey type", "CCS PL",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, surveyType);
      }
    }
  }

  @Override
  @Transactional
  public void createCcsInterviewOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "CCS INT";
    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_INT_OUTCOME,
            "Survey type", "CCS INT",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, surveyType);
      }
    }
  }

  @Override
  @Transactional
  public void createNcOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    final String surveyType = "NC";
    if (operationsList == null) {
      triggerError(outcome, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_NC_OUTCOME,
            "Survey type", "NC",
            "Secondary Outcome", outcome.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, surveyType);
      }
    }
  }
}
