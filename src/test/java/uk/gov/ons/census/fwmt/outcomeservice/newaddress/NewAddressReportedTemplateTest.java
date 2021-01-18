package uk.gov.ons.census.fwmt.outcomeservice.newaddress;

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
import uk.gov.ons.census.fwmt.outcomeservice.converter.impl.NewAddressReportedProcessor;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;
import uk.gov.ons.census.fwmt.outcomeservice.helpers.OutcomeHelper;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;

import java.text.DateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewAddressReportedTemplateTest {

  @InjectMocks
  private NewAddressReportedProcessor newAddressReportedProcessor;

  @Mock
  private GatewayCacheService cacheService;

  @Mock
  private GatewayEventManager eventManager;

  @Mock
  private DateFormat dateFormat;

  @Mock
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Captor
  private ArgumentCaptor<String> outcomeEventCaptor;

  @Test
  @DisplayName("Should update the estabType for SPG")
  public void shouldUpdateEstabTypeForSpg() throws GatewayException, JSONException {
    final OutcomeSuperSetDto outcome = new OutcomeHelper().createNewStandaloneOutcome();
    when(dateFormat.format(any())).thenReturn("2020-04-17T11:53:11.000+0000");
    newAddressReportedProcessor.process(outcome, outcome.getCaseId(), "SPG");
    verify(gatewayOutcomeProducer).sendOutcome(outcomeEventCaptor.capture(), any(), any());
    String outcomeEvent = outcomeEventCaptor.getValue();
    JSONObject jsonObject = new JSONObject(outcomeEvent);
    JSONObject collectionCase = jsonObject.getJSONObject("payload").getJSONObject("newAddress").getJSONObject("collectionCase").getJSONObject("address");
    String estabType = collectionCase.get("estabType").toString();
    Assertions.assertEquals(outcome.getCeDetails().getEstablishmentType(), estabType);
  }

  @Test
  @DisplayName("Should update the estabType for CE")
  public void shouldUpdateEstabTypeForCe() throws GatewayException, JSONException {
    final OutcomeSuperSetDto outcome = new OutcomeHelper().createNewStandaloneOutcome();
    when(dateFormat.format(any())).thenReturn("2020-04-17T11:53:11.000+0000");
    newAddressReportedProcessor.process(outcome, outcome.getCaseId(), "CE");
    verify(gatewayOutcomeProducer).sendOutcome(outcomeEventCaptor.capture(), any(), any());
    String outcomeEvent = outcomeEventCaptor.getValue();
    JSONObject jsonObject = new JSONObject(outcomeEvent);
    JSONObject collectionCase = jsonObject.getJSONObject("payload").getJSONObject("newAddress").getJSONObject("collectionCase").getJSONObject("address");
    String estabType = collectionCase.get("estabType").toString();
    Assertions.assertEquals(outcome.getCeDetails().getEstablishmentType(), estabType);
  }
}