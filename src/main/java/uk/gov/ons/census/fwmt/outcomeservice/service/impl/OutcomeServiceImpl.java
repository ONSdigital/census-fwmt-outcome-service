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

import javax.transaction.Transactional;


@Slf4j
@Service
public class OutcomeServiceImpl implements OutcomeService {

  public static final String PROCESSING_HH_OUTCOME = "PROCESSING_HH_OUTCOME";

  public static final String PROCESSING_SPG_OUTCOME = "PROCESSING_SPG_OUTCOME";

  public static final String PROCESSING_CE_OUTCOME = "PROCESSING_CE_OUTCOME";

  public static final String PROCESSING_CCS_PL_OUTCOME = "PROCESSING_CCS_PL_OUTCOME";

  public static final String PROCESSING_CCS_INT_OUTCOME = "PROCESSING_CCS_INT_OUTCOME";

  public static final String FAILED_TO_LOOKUP_OUTCOME_CODE = "FAILED_TO_LOOKUP_OUTCOME_CODE";

  private static final String NO_OUTCOME_CODE_FOUND = "No outcome code found";

  private static final String SURVEY_TYPE = "Survey type";

  private static final String SECONDARY_OUTCOME = "Secondary Outcome";

  private static final String OPERATION = "Operation";

  private static final String OPERATION_LIST = "Operation list";

  private static final String OUTCOME_CODE = "Outcome code";
  private static final String CCS_INT = "CCS INT";

  private static final String CCS_PL = "CCS PL";

  private static final String HH = "HH";

  private static final String CE = "CE";

  private static final String SPG = "SPG";

  private static final String HELD_CASE_ID = "Held Case ID";

  @Autowired
  private Map<String, OutcomeServiceProcessor> outcomeServiceProcessors;

  @Autowired
  private OutcomeLookup outcomeLookup;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  @Transactional
  public void createSpgOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null || operationsList.length == 0) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, NO_OUTCOME_CODE_FOUND,
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          SURVEY_TYPE, SPG,
          OUTCOME_CODE, outcome.getOutcomeCode(),
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_SPG_OUTCOME,
          SURVEY_TYPE, SPG,
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription(),
          HELD_CASE_ID, (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          OPERATION, operation,
          OPERATION_LIST, Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, SPG);
    }
  }

  @Override
  @Transactional
  public void createCeOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null || operationsList.length == 0) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, NO_OUTCOME_CODE_FOUND,
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          SURVEY_TYPE, CE,
          OUTCOME_CODE, outcome.getOutcomeCode(),
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CE_OUTCOME,
          SURVEY_TYPE, CE,
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription(),
          HELD_CASE_ID, (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          OPERATION, operation,
          OPERATION_LIST, Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, CE);
    }
  }

  @Override
  @Transactional
  public void createHhOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null || operationsList.length == 0) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, NO_OUTCOME_CODE_FOUND,
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          SURVEY_TYPE, HH,
          OUTCOME_CODE, outcome.getOutcomeCode(),
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_HH_OUTCOME,
          SURVEY_TYPE, "HH",
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription(),
          HELD_CASE_ID, (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          OPERATION, operation,
          OPERATION_LIST, Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, "HH");
    }
  }

  @Override
  @Transactional
  public void createCcsPropertyListingOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null || operationsList.length == 0) {
      triggerOutcomeCodeError(outcome, CCS_PL);
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_PL_OUTCOME,
          SURVEY_TYPE, CCS_PL,
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription(),
          HELD_CASE_ID, (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          OPERATION, operation,
          OPERATION_LIST, Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, CCS_PL);
    }
  }

  private void triggerOutcomeCodeError(OutcomeSuperSetDto outcome, String ccsPl) {
    gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, NO_OUTCOME_CODE_FOUND,
        String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
        SURVEY_TYPE, ccsPl,
        OUTCOME_CODE, outcome.getOutcomeCode(),
        SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription());
  }

  @Override
  @Transactional
  public void createCcsInterviewOutcomeEvent(OutcomeSuperSetDto outcome) throws GatewayException {
    String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null || operationsList.length == 0) {
      gatewayEventManager.triggerErrorEvent(this.getClass(), (Exception) null, NO_OUTCOME_CODE_FOUND,
          String.valueOf(outcome.getCaseId()), FAILED_TO_LOOKUP_OUTCOME_CODE,
          SURVEY_TYPE, CCS_INT,
          OUTCOME_CODE, outcome.getOutcomeCode(),
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription());
      return;
    }
    UUID caseIdHolder = null;
    for (String operation : operationsList) {
      gatewayEventManager.triggerEvent(String.valueOf(outcome.getCaseId()), PROCESSING_CCS_INT_OUTCOME,
          SURVEY_TYPE, CCS_INT,
          SECONDARY_OUTCOME, outcome.getSecondaryOutcomeDescription(),
          HELD_CASE_ID, (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
          OPERATION, operation,
          OPERATION_LIST, Arrays.toString(operationsList));
      caseIdHolder = outcomeServiceProcessors.get(operation).process(outcome, caseIdHolder, CCS_INT);
    }
  }
}
