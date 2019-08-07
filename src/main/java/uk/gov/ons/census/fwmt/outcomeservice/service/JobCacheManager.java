package uk.gov.ons.census.fwmt.outcomeservice.service;


import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLOutcomeEntity;

public interface JobCacheManager {

  CCSPLOutcomeEntity cacheCCSOutcome(CCSPLOutcomeEntity ccsOutcomeEntity);

}
