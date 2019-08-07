package uk.gov.ons.census.fwmt.outcomeservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

@Component
public class CCSPLStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  public CCSPLOutcomeEntity cacheJob(String caseId, String ccsPropertyListingOutcome) {
    CCSPLOutcomeEntity ccsOutcomeEntity = new CCSPLOutcomeEntity();

    ccsOutcomeEntity.setCaseId(caseId);
    ccsOutcomeEntity.setCcsPropertyListingOutcome(ccsPropertyListingOutcome);

    return jobCacheManager.cacheCCSOutcome(ccsOutcomeEntity);
  }
}
