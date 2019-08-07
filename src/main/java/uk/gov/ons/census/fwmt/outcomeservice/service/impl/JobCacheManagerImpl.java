package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLOutcomeEntity;
import uk.gov.ons.census.fwmt.outcomeservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil<CCSPLOutcomeEntity> redisUtil;

  @Override
  public CCSPLOutcomeEntity cacheCCSOutcome(CCSPLOutcomeEntity ccsOutcomeEntity) {
    redisUtil.putValue(ccsOutcomeEntity.getCaseId(), ccsOutcomeEntity);
    log.info("Placed the following in cache: " +  ccsOutcomeEntity.toString());
    return ccsOutcomeEntity;
  }
}
