package uk.gov.ons.census.fwmt.outcomeservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_CACHED_OUTCOME;

@Slf4j
@Component
public class CCSPLStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private GatewayEventManager eventManager;

  public void cacheJob(String caseId, CCSPropertyListingOutcome ccsPLOutcome) throws JsonProcessingException {

    String stringOutcome = objectMapper.writeValueAsString(ccsPLOutcome);

    jobCacheManager.cacheCCSOutcome(caseId, stringOutcome);
    eventManager.triggerEvent(caseId,CCSPL_CACHED_OUTCOME);
  }
}
