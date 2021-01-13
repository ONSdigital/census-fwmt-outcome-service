package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

    import org.junit.Assert;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.MockitoJUnitRunner;
    import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;

    import static org.mockito.ArgumentMatchers.anyString;
    import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NCTMCaseIdOverrideTest {

  @InjectMocks
  private NCTMCaseIdOverride nctmCaseIdOverride;

  @Mock
  private GatewayCacheService gatewayCacheService;

  @Test
  public void shouldOverrideTMCaseIdWithRMOriginalCaseId() {
    GatewayCache gatewayCache = new GatewayCache();
    gatewayCache.setOriginalCaseId("a48bf28e-e7f4-4467-a9fb-e000b6a55676");
    when(gatewayCacheService.getById(anyString())).thenReturn(gatewayCache);
    String hhCaseId = nctmCaseIdOverride.overrideTMCaseIdWithRMOriginalCaseId("b48bf28e-e7f4-4467-a9fb-e000b6a33543");
    Assert.assertEquals("a48bf28e-e7f4-4467-a9fb-e000b6a55676",hhCaseId);
    verify(gatewayCacheService, times(1)).getById(anyString());
  }
}