package uk.gov.ons.census.fwmt.outcomeservice.helpers;

import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
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

  public OutcomeSuperSetDto createHardRefusalOutcomneWithNullDangerous() {
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
    refusal.setHouseholder(true);
    outcomeSuperSetDto.setRefusal(refusal);
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    return outcomeSuperSetDto;
  }

  public OutcomeSuperSetDto createHardRefusalOutcomeWithSite() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    outcomeSuperSetDto.setCaseId(UUID.fromString("e04cbf10-597b-11eb-ae93-0242ac130002"));
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
    outcomeSuperSetDto.setSiteCaseId(UUID.fromString("bd6345af-d706-43d3-a13b-8c549e081a76"));
    return outcomeSuperSetDto;
  }

  public OutcomeSuperSetDto createHardRefusalWithOutcomeCode010307() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    outcomeSuperSetDto.setCaseId(UUID.randomUUID());
    outcomeSuperSetDto.setOutcomeCode("01-03-07");
    List<CareCodeDto> careCodeDto = new ArrayList<>();
    careCodeDto.add(CareCodeDto.builder().careCode("Test123").build());
    outcomeSuperSetDto.setCareCodes(careCodeDto);
    outcomeSuperSetDto.setAccessInfo("12345");
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    return outcomeSuperSetDto;
  }

  public OutcomeSuperSetDto createHardRefusalWithoutRefusalObject() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    outcomeSuperSetDto.setCaseId(UUID.randomUUID());
    outcomeSuperSetDto.setOutcomeCode("03-02-01");
    List<CareCodeDto> careCodeDto = new ArrayList<>();
    careCodeDto.add(CareCodeDto.builder().careCode("Test123").build());
    outcomeSuperSetDto.setCareCodes(careCodeDto);
    outcomeSuperSetDto.setAccessInfo("12345");
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    return outcomeSuperSetDto;
  }
}
