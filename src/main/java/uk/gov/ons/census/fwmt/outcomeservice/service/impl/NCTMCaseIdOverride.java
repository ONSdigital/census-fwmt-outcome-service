package uk.gov.ons.census.fwmt.outcomeservice.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
@Component
public class NCTMCaseIdOverride {
    @Autowired
    GatewayCacheService gatewayCacheService;
    public String overrideTMCaseIdWithRMOriginalCaseId(String caseID) {
        GatewayCache gatewayCache = gatewayCacheService.getById(caseID);
        return gatewayCache.getOriginalCaseId();
    }
}