package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
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

  private final MapperFacade mapperFacade;

  @Override
  @Transactional
  public void createSpgOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "SPG";
    executeOperations(outcome, surveyType, PROCESSING_SPG_OUTCOME);
  }

  @Override
  @Transactional
  public void createCeOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "CE";
    executeOperations(outcome, surveyType, PROCESSING_CE_OUTCOME);
  }

  @Override
  @Transactional
  public void createHhOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "HH";
    executeOperations(outcome, surveyType, PROCESSING_HH_OUTCOME);
  }

  @Override
  @Transactional
  public void createCcsPropertyListingOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "CCS PL";
    executeOperations(outcome, surveyType, PROCESSING_CCS_PL_OUTCOME);
  }

  @Override
  @Transactional
  public void createCcsInterviewOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "CCS INT";
    executeOperations(outcome, surveyType, PROCESSING_CCS_INT_OUTCOME);
  }

  @Override
  @Transactional
  public void createNcOutcomeEvent(CommonOutcome outcome) throws GatewayException {
    final String surveyType = "NC";
    executeOperations(outcome, surveyType, PROCESSING_NC_OUTCOME);
  }

  private void executeOperations(CommonOutcome outcome, String surveyType, String eventType) throws GatewayException {
    final OutcomeSuperSetDto outcomeDTO = mapperFacade.map(outcome, OutcomeSuperSetDto.class);

    final String[] operationsList = outcomeLookup.getLookup(outcome.getOutcomeCode());
    if (operationsList == null) {
      triggerError(outcomeDTO, surveyType);
    } else {
      UUID caseIdHolder = null;
      for (String operation : operationsList) {
        gatewayEventManager.triggerEvent(String.valueOf(outcomeDTO.getCaseId()), eventType,
            "Survey type", surveyType,
            "Secondary Outcome", outcomeDTO.getSecondaryOutcomeDescription(),
            "Held case id", (caseIdHolder != null) ? String.valueOf(caseIdHolder) : "N/A",
            "Operation", operation,
            "Operation list", Arrays.toString(operationsList));
        caseIdHolder = outcomeServiceProcessors.get(operation).process(outcomeDTO, caseIdHolder, surveyType);
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
}
