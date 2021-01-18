package uk.gov.ons.census.fwmt.outcomeservice.helpers;

import uk.gov.ons.census.fwmt.common.data.shared.CeDetails;
import uk.gov.ons.census.fwmt.outcomeservice.dto.CareCodeDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.CeDetailsDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OutcomeHelper {

  public OutcomeSuperSetDto createUpdateResidentCountZero() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    CeDetailsDto ceDetailsDto = new CeDetailsDto();
    outcomeSuperSetDto.setCaseId(UUID.randomUUID());
    outcomeSuperSetDto.setOutcomeCode("Test");
    List<CareCodeDto> careCodeDto = new ArrayList<>();
    careCodeDto.add(CareCodeDto.builder().careCode("Test123").build());
    outcomeSuperSetDto.setCareCodes(careCodeDto);
    outcomeSuperSetDto.setAccessInfo("12345");
    outcomeSuperSetDto.setSiteCaseId(UUID.fromString("e04cbf10-597b-11eb-ae93-0242ac130002"));
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    ceDetailsDto.setUsualResidents(1);
    outcomeSuperSetDto.setCeDetails(ceDetailsDto);
    return outcomeSuperSetDto;
  }
}
