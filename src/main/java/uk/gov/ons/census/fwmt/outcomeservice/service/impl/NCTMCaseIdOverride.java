package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;

import java.util.UUID;

@Component
public class NCTMCaseIdOverride {

    @Autowired
    private GatewayCacheService gatewayCacheService;

    public void overrideTMCaseIdWithRMOriginalCaseId(String caseID, NCOutcome ncOutcome) {
        GatewayCache gatewayCache = gatewayCacheService.getById(caseID);
        ncOutcome.setCaseId(UUID.fromString(gatewayCache.getOriginalCaseId()));
    }
}
