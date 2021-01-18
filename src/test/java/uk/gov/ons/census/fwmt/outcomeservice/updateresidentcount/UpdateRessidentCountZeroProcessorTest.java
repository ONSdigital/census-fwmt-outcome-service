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
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.UpdateResidentCountZeroProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.helpers.OutcomeHelper;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.template.TemplateCreator;

import java.text.DateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateRessidentCountZeroProcessorTest {

  @InjectMocks
  private UpdateResidentCountZeroProcessor updateResidentCountZeroProcessor;

  @Mock
  private GatewayCacheService cacheService;

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

  @Captor
  private ArgumentCaptor<String> outcomeEventCaptor;

  @Test
  @DisplayName("Should update the site resident count to zero")
  public void shouldUpdateSiteResidentCountToZero() throws GatewayException, ParseException, JSONException {
    final OutcomeSuperSetDto outcome = new OutcomeHelper().createUpdateResidentCountZero();
    when(dateFormat.format(any())).thenReturn("2020-04-17T11:53:11.000+0000");
    updateResidentCountZeroProcessor.process(outcome, outcome.getCaseId(), "CE");
    verify(gatewayOutcomeProducer).sendOutcome(outcomeEventCaptor.capture(), any(), any());
    String outcomeEvent = outcomeEventCaptor.getValue();
    JSONObject jsonObject = new JSONObject(outcomeEvent);

    JSONObject collectionCase = jsonObject.getJSONObject("payload").getJSONObject("collectionCase");
    String siteId = collectionCase.get("id").toString();
    String ceExpectedCapacity = collectionCase.get("ceExpectedCapacity").toString();
    Assertions.assertEquals(outcome.getSiteCaseId().toString(), siteId);
    Assertions.assertEquals("0", ceExpectedCapacity);
  }
}