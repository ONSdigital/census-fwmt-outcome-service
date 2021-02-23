package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.OutcomeServiceProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutcomeServiceImplTest {

  private OutcomeServiceImpl outcomeService;

  @Mock
  private OutcomeLookup outcomeLookupMock;

  @Mock
  private GatewayEventManager gwEventManagerMock;

  @Mock
  private OutcomeServiceProcessor processor;

  @Mock
  private MapperFacade mapperFacade;

  private OutcomeSuperSetDto createOutcomeDTO() {
    final OutcomeSuperSetDto outcome = new OutcomeSuperSetDto();
    outcome.setCaseId(UUID.randomUUID());
    outcome.setOutcomeCode("TEST_OUTCOMECODE");
    return outcome;
  }

  private CommonOutcome createCommonOutcome() {
    CommonOutcome commonOutcome = new CommonOutcome();
    commonOutcome.setCaseId(UUID.randomUUID());
    commonOutcome.setOutcomeCode("TEST_OUTCOMECODE");
    return commonOutcome;
  }

  @BeforeEach
  void setup() {
    final Map<String, OutcomeServiceProcessor> outcomeServiceProcessors = new HashMap();
    outcomeServiceProcessors.put("processor1", processor);
    outcomeServiceProcessors.put("processor2", processor);
    outcomeService = new OutcomeServiceImpl(outcomeServiceProcessors, outcomeLookupMock, gwEventManagerMock, mapperFacade);
  }

  @Test
  void shouldCreateSpgOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "SPG";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();
    createTest(myUUID, type, commonOutcome,outcome);
    outcomeService.createSpgOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  @Test
  void shouldCreateCeOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "CE";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();
    createTest(myUUID, type, commonOutcome,outcome);
    outcomeService.createCeOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  @Test
  void shouldCreateHhOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "HH";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();
    createTest(myUUID, type, commonOutcome, outcome);
    outcomeService.createHhOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  @Test
  void shouldCreateCcsPropertyListingOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "CCS PL";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();
    createTest(myUUID, type, commonOutcome,outcome);
    outcomeService.createCcsPropertyListingOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  @Test
  void shouldCreateCcsInterviewOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "CCS INT";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();
    createTest(myUUID, type, commonOutcome,outcome);
    outcomeService.createCcsInterviewOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  @Test
  void createNcOutcomeEvent() throws GatewayException {
    UUID myUUID = UUID.randomUUID();
    String type = "NC";
    OutcomeSuperSetDto outcome = createOutcomeDTO();
    CommonOutcome commonOutcome = createCommonOutcome();

    createTest(myUUID, type, commonOutcome,outcome);
    outcomeService.createNcOutcomeEvent(commonOutcome);
    createVerifyRules(myUUID, type, outcome);
  }

  private void createVerifyRules(UUID myUUID, String type, OutcomeSuperSetDto outcome) throws GatewayException {
    verify(processor).process(eq(outcome), eq(null), eq(type));
    verify(processor).process(eq(outcome), eq(myUUID), eq(type));
  }

  private void createTest(UUID myUUID, String type, CommonOutcome outcome, OutcomeSuperSetDto outcomeSuperSetDto) throws GatewayException {
    final String[] lookups = {"processor1", "processor2"};

    when(mapperFacade.map(eq(outcome), eq(OutcomeSuperSetDto.class))).thenReturn(outcomeSuperSetDto);
    when(outcomeLookupMock.getLookup(eq("TEST_OUTCOMECODE"))).thenReturn(lookups);
    when(processor.process(eq(outcomeSuperSetDto), eq(null), eq(type))).thenReturn(myUUID);
  }
}