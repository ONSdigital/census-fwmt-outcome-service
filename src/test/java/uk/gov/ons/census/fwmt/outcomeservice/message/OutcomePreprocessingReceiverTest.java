package uk.gov.ons.census.fwmt.outcomeservice.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HHNewSplitAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHOutcome;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.common.data.shared.CommonOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutcomePreprocessingReceiverTest {
  @InjectMocks
  private OutcomePreprocessingReceiver receiver;
  @Mock
  private GatewayEventManager eventManager;
  @Mock
  private OutcomeService delegate;

  @Captor
  private ArgumentCaptor<CommonOutcome> outcomeArgumentCaptor;

  @Test
  void shouldProcessMessage_SPGOutcome() throws GatewayException {
    final SPGOutcome outcome = new SPGOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createSpgOutcomeEvent(eq(outcome));
    verify(delegate).createSpgOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should not change");

  }

  @Test
  void testProcessMessage_SPGNewUnitAddress() throws GatewayException {
    SPGNewUnitAddress outcome = new SPGNewUnitAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createSpgOutcomeEvent(eq(outcome));
    verify(delegate).createSpgOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_SPGNewStandaloneAddress() throws GatewayException {
    SPGNewStandaloneAddress outcome = new SPGNewStandaloneAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createSpgOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");
  }

  @Test
  void testProcessMessage_CEOutcome() throws GatewayException {
    CEOutcome outcome = new CEOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createCeOutcomeEvent(eq(outcome));
    verify(delegate).createCeOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should not be changed");

  }

  @Test
  void testProcessMessage_CENewUnitAddress() throws GatewayException {
    CENewUnitAddress outcome = new CENewUnitAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createCeOutcomeEvent(eq(outcome));
    verify(delegate).createCeOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_CENewStandaloneAddress() throws GatewayException {
    CENewStandaloneAddress outcome = new CENewStandaloneAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createCeOutcomeEvent(eq(outcome));
    verify(delegate).createCeOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_HHOutcome() throws GatewayException {
    HHOutcome outcome = new HHOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createHhOutcomeEvent(eq(outcome));
    verify(delegate).createHhOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should not be reset");

  }

  @Test
  void testProcessMessage_HHNewSplitAddress() throws GatewayException {
    HHNewSplitAddress outcome = new HHNewSplitAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createHhOutcomeEvent(eq(outcome));
    verify(delegate).createHhOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_HHNewStandaloneAddress() throws GatewayException {
    HHNewStandaloneAddress outcome = new HHNewStandaloneAddress();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createHhOutcomeEvent(eq(outcome));
    verify(delegate).createHhOutcomeEvent(outcomeArgumentCaptor.capture());
    assertNotEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_CCSPropertyListingOutcome() throws GatewayException {
    CCSPropertyListingOutcome outcome = new CCSPropertyListingOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createCcsPropertyListingOutcomeEvent(eq(outcome));
    verify(delegate).createCcsPropertyListingOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be changed");

  }

  @Test
  void testProcessMessage_CCSInterviewOutcome() throws GatewayException {
    CCSInterviewOutcome outcome = new CCSInterviewOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createCcsInterviewOutcomeEvent(eq(outcome));
    verify(delegate).createCcsInterviewOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }

  @Test
  void testProcessMessage_NCOutcome() throws GatewayException {
    NCOutcome outcome = new NCOutcome();
    UUID randomId = UUID.randomUUID();
    outcome.setCaseId(randomId);
    receiver.processMessage(outcome);
    verify(delegate).createNcOutcomeEvent(eq(outcome));
    verify(delegate).createNcOutcomeEvent(outcomeArgumentCaptor.capture());
    assertEquals(randomId, outcomeArgumentCaptor.getValue().getCaseId(), "Rule not adhered to , ID should be set");

  }
}