package uk.gov.ons.census.fwmt.outcomeservice.hhfeedbacklongpause;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtCancelActionInstruction;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomeSetup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.RefusalEncryptionLookup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.CancelHHFeedbackLongPauseProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.HardRefusalReceivedProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.helpers.HardRefusalHelper;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.message.RmFieldRepublishProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CancelHHFeedbackLongPauseProcessorTest {

  @InjectMocks
  private CancelHHFeedbackLongPauseProcessor cancelHHFeedbackLongPauseProcessor;

  @Mock
  private RmFieldRepublishProducer rmFieldRepublishProducer;

  @Mock
  private GatewayCache gatewayCache;

  @Mock
  private GatewayEventManager eventManager;

  @Mock
  private TemplateCreator temmplateCreator;

  @Mock
  private RefusalEncryptionLookup refusalEncryptionLookup;

  @Mock
  private DateFormat dateFormat;

  @Mock
  private Date date;

  @Mock
  private OutcomeSetup outcomeSetup;

  @Mock
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Captor
  private ArgumentCaptor<FwmtCancelActionInstruction> longPause;

  @Test
  @DisplayName("Should send FwmtCancelActionInstruction to RM")
  public void shouldSendFwmtCancelActionInstructionToRm() throws GatewayException {
    final OutcomeSuperSetDto outcome = new HardRefusalHelper().createHardRefusalOutcomne();
    Assertions.assertEquals(outcome.getCaseId(), cancelHHFeedbackLongPauseProcessor.process(outcome, outcome.getCaseId(), "HH"));
  }
}