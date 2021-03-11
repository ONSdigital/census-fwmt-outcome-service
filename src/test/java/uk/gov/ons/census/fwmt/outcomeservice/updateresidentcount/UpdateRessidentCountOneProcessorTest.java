package uk.gov.ons.census.fwmt.outcomeservice.updateresidentcount;

import org.apache.tomcat.util.json.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
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
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.UpdateResidentCountOneProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.helpers.OutcomeHelper;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateRessidentCountOneProcessorTest {

  @InjectMocks
  private UpdateResidentCountOneProcessor updateRessidentCountOneProcessor;

  @Mock
  private GatewayCache gatewayCache;

  @Mock
  private GatewayEventManager eventManager;

  @Mock
  private TemplateCreator temmplateCreator;

  @Mock
  private DateFormat dateFormat;

  @Mock
  private Date date;

  @Mock
  private OutcomeSetup outcomeSetup;

  @Mock
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Mock
  private GatewayCacheService gatewayCacheService;

  @Captor
  private ArgumentCaptor<GatewayCache> spiedCache;

  @Captor
  private ArgumentCaptor<String> outcomeEventCaptor;

  @Test
  @DisplayName("Should update the closed cache state to update")
  public void shouldUpdateTheClosedCacheStateToUpdate() throws GatewayException, ParseException, JSONException {
    final OutcomeSuperSetDto outcome = new OutcomeHelper().createUpdateResidentOneCount();
    when(dateFormat.format(any())).thenReturn("2020-04-17T11:53:11.000+0000");
    GatewayCache gatewayCache = new GatewayCache();
    gatewayCache.setOriginalCaseId(outcome.getCaseId().toString());
    gatewayCache.setLastActionInstruction("CANCEL");
    when(gatewayCacheService.getById(anyString())).thenReturn(gatewayCache);
    updateRessidentCountOneProcessor.process(outcome, outcome.getCaseId(), "CE");
    verify(gatewayCacheService).save(spiedCache.capture());
    String lastActionInstruction = spiedCache.getValue().lastActionInstruction;
    Assertions.assertEquals("UPDATE", lastActionInstruction);
  }
}