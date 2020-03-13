package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_CACHED_FAILED;
import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.CCSPL_CACHED_OUTCOME;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil<String> redisUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private GatewayEventManager eventManager;

  @Override
  public void cacheCCSOutcome(String caseId, CCSPropertyListingOutcome ccsPLOutcome) throws GatewayException {
    String outcome;
    try {
      outcome = objectMapper.writeValueAsString(ccsPLOutcome);
    } catch (JsonProcessingException e) {
      eventManager.triggerErrorEvent(this.getClass(), e,"Failed to cache", caseId, CCSPL_CACHED_FAILED);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR,
          "Unable to cache CCS PL Outcome for caseId: {}", caseId);
    }
    redisUtil.putValue(caseId, outcome);
    eventManager.triggerEvent(caseId, CCSPL_CACHED_OUTCOME);
  }
}
