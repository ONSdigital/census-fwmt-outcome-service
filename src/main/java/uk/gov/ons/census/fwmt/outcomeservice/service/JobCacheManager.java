package uk.gov.ons.census.fwmt.outcomeservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import uk.gov.ons.census.fwmt.outcomeservice.redis.CCSPLOutcomeEntity;

public interface JobCacheManager {

  String cacheCCSOutcome(String caseId, String outcome) throws JsonProcessingException;

}
