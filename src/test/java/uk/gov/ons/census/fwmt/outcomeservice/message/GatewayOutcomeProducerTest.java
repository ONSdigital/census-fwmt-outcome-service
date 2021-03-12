package uk.gov.ons.census.fwmt.outcomeservice.hardrefusal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomeSetup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.RefusalEncryptionLookup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.HardRefusalReceivedProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.helpers.HardRefusalHelper;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HardRefusalReceivedProcessorTest {

  @InjectMocks
  private HardRefusalReceivedProcessor hardRefusalReceivedProcessor;

  @Mock
  private GatewayCacheService cacheService;

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
  private ArgumentCaptor<GatewayCache> spiedCache;

  @Test
  @DisplayName("Should update the cache")
  public void shouldUpdateHareRefusalCache() throws GatewayException {
    final OutcomeSuperSetDto outcome = new HardRefusalHelper().createHardRefusalOutcomne();
    GatewayCache mockEntry = new GatewayCache();
    mockEntry.setCaseId(outcome.getCaseId().toString());
    when(cacheService.getById(outcome.getCaseId().toString())).thenReturn(mockEntry);
    when(dateFormat.format(any())).thenReturn("2020-04-17T11:53:11.000+0000");
    hardRefusalReceivedProcessor.process(outcome, outcome.getCaseId(), "HH");
    verify(cacheService).save(spiedCache.capture());
    String caseId = spiedCache.getValue().caseId;
    String careCodes = spiedCache.getValue().careCodes;
    String accessInfo = spiedCache.getValue().accessInfo;
    Assertions.assertEquals(outcome.getCaseId().toString(), caseId);
    Assertions.assertEquals("Test123, Dangerous address", careCodes);
    Assertions.assertEquals(outcome.getAccessInfo(), accessInfo);
  }

  @Test
  @DisplayName("Should throw an error if the cache does not exist")
  public void shouldThrowErrorIfCacheDoesNotExistForHardRefusal() throws GatewayException {
    final OutcomeSuperSetDto outcome = new HardRefusalHelper().createHardRefusalOutcomne();
    Assertions.assertThrows(GatewayException.class, () -> {
      hardRefusalReceivedProcessor.process(outcome, outcome.getCaseId(), "HH");
    });
  }

  @Test
  @DisplayName("IsDangerous field should return false is null")
  public void shouldReturnIsDangerousAsFalseIfNull() {
    final OutcomeSuperSetDto outcome = new HardRefusalHelper().createHardRefusalOutcomneWithNullDangerous();
    Assertions.assertEquals(false, outcome.getRefusal().isDangerous());
  }
}