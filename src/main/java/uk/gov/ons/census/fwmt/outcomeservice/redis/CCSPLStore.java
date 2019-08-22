package uk.gov.ons.census.fwmt.outcomeservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.Address;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CareCode;
import uk.gov.ons.census.fwmt.common.data.ccs.CeDetails;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

import java.util.List;

@Component
public class CCSPLStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  @Autowired
  private ObjectMapper objectMapper;

  private List careCodeList;

  public CCSPLOutcomeEntity cacheJob(String caseId, CCSPropertyListingOutcome ccsPLOutcome) throws JsonProcessingException {

    CCSPropertyListingOutcome ccsPLOutcomeToStore = new CCSPropertyListingOutcome();
    CCSPLOutcomeEntity ccsOutcomeEntity = new CCSPLOutcomeEntity();

    Address address = new Address();
    CareCode careCode = new CareCode();
    CeDetails ceDetails = new CeDetails();

    address.setOa(ccsPLOutcome.getAddress().getOa());

    ccsPLOutcomeToStore.setAddress(address);
    if (ccsPLOutcome.getCeDetails() != null) {
      ceDetails.setManagerName(ccsPLOutcome.getCeDetails().getManagerName());
      ceDetails.setUsualResidents(ccsPLOutcome.getCeDetails().getUsualResidents());
      ceDetails.setBedspaces(ccsPLOutcome.getCeDetails().getBedspaces());
      ceDetails.setContactPhone(ccsPLOutcome.getCeDetails().getContactPhone());
      ccsPLOutcomeToStore.setCeDetails(ceDetails);
    }
    ccsPLOutcomeToStore.setAccessInfo(ccsPLOutcome.getAccessInfo());

    ccsPLOutcomeToStore.setCareCodes(ccsPLOutcome.getCareCodes());

    String stringOutcome = objectMapper.writeValueAsString(ccsPLOutcomeToStore);

    ccsOutcomeEntity.setCaseId(caseId);
    ccsOutcomeEntity.setCcsPropertyListingOutcome(stringOutcome);

    return jobCacheManager.cacheCCSOutcome(ccsOutcomeEntity);

  }

}
