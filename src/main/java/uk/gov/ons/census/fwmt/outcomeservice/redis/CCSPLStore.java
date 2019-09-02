package uk.gov.ons.census.fwmt.outcomeservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingCached;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

@Component
public class CCSPLStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  @Autowired
  private ObjectMapper objectMapper;

  public String cacheJob(String caseId, CCSPropertyListingOutcome ccsPLOutcome) throws JsonProcessingException {

    CCSPropertyListingCached ccsPropertyListingCached = new CCSPropertyListingCached();

    ccsPropertyListingCached.setPrimaryOutcome(ccsPLOutcome.getPrimaryOutcome());
    ccsPropertyListingCached.setSecondaryOutcome(ccsPLOutcome.getSecondaryOutcome());
    ccsPropertyListingCached.setOa(ccsPLOutcome.getAddress().getOa());
    if (ccsPLOutcome.getCeDetails() != null) {
      ccsPropertyListingCached.setManagerName(ccsPLOutcome.getCeDetails().getManagerName());
      ccsPropertyListingCached.setUsualResidents(ccsPLOutcome.getCeDetails().getUsualResidents());
      ccsPropertyListingCached.setBedspaces(ccsPLOutcome.getCeDetails().getBedspaces());
      ccsPropertyListingCached.setContactPhone(ccsPLOutcome.getCeDetails().getContactPhone());
    }
    ccsPropertyListingCached.setAccessInfo(ccsPLOutcome.getAccessInfo());
    ccsPropertyListingCached.setCareCodes(ccsPLOutcome.getCareCodes());

    String stringOutcome = objectMapper.writeValueAsString(ccsPropertyListingCached);

    return jobCacheManager.cacheCCSOutcome(caseId, stringOutcome);

  }

}
