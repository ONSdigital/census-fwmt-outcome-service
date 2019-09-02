package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.outcomeservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;


@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public String cacheCCSOutcome(String caseId, String outcome) {
    redisUtil.putValue(caseId, outcome);
    log.info("Placed the following in cache: " +  outcome);
    return outcome;
  }
}
