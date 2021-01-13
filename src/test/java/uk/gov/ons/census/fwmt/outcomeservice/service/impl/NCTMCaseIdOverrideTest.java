package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
        NCOutcome ncOutcome = new NCOutcome();

        when(gatewayCacheService.getById(anyString())).thenReturn(gatewayCache);
        nctmCaseIdOverride.overrideTMCaseIdWithRMOriginalCaseId("b48bf28e-e7f4-4467-a9fb-e000b6a33543", ncOutcome);
        assertThat("a48bf28e-e7f4-4467-a9fb-e000b6a55676", is(equalTo(ncOutcome.getCaseId().toString())));
        verify(gatewayCacheService, times(1)).getById(anyString());
    }
}
