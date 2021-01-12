package uk.gov.ons.census.fwmt.outcomeservice.helpers;

import uk.gov.ons.census.fwmt.common.data.shared.Refusal;
import uk.gov.ons.census.fwmt.outcomeservice.dto.CareCodeDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HardRefusalHelper {

  public OutcomeSuperSetDto createHardRefusalOutcomne() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    outcomeSuperSetDto.setCaseId(UUID.randomUUID());
    outcomeSuperSetDto.setOutcomeCode("Test");
    List<CareCodeDto> careCodeDto = new ArrayList<>();
    careCodeDto.add(CareCodeDto.builder().careCode("Test123").build());
    outcomeSuperSetDto.setCareCodes(careCodeDto);
    outcomeSuperSetDto.setAccessInfo("12345");
    Refusal refusal = new Refusal();
    refusal.setTitle("Mr");
    refusal.setFirstname("John");
    refusal.setSurname("Smith");
    refusal.setDangerous(true);
    refusal.setHouseholder(true);
    outcomeSuperSetDto.setRefusal(refusal);
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    return outcomeSuperSetDto;
  }
}
