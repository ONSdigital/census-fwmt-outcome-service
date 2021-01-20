package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;

import javax.transaction.Transactional;

@Component
public class SwitchCaseIdService {

    @Autowired
    private GatewayCacheService gatewayCacheService;

    @Transactional
    public String fromNcToOriginal(String caseID) {
        GatewayCache gatewayCache = gatewayCacheService.getById(caseID);
        return gatewayCache.getOriginalCaseId();
    }

    @Transactional
    public String fromIdOriginalToNc(String caseId) {
        GatewayCache gatewayCache = gatewayCacheService.getByOriginalId(caseId);
        return gatewayCache.getCaseId();
    }
}
