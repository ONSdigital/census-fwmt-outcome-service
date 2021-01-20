package uk.gov.ons.census.fwmt.outcomeservice.helpers;

import uk.gov.ons.census.fwmt.common.data.shared.Address;
import uk.gov.ons.census.fwmt.common.data.shared.CeDetails;
import uk.gov.ons.census.fwmt.outcomeservice.dto.CareCodeDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.CeDetailsDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.math.BigDecimal;
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

  private Address createAddress(){
    Address address = new Address();
    address.setAddressLine1("Unit name");
    address.setAddressLine2("Some house name");
    address.setAddressLine3("some road");
    address.setLocality("Some town");
    address.setPostcode("AA1 2BB");
    address.setLatitude(BigDecimal.valueOf(50.00000));
    address.setLongitude(BigDecimal.valueOf(1.00000));
    return address;
  }

  public OutcomeSuperSetDto createNewStandaloneOutcome() {
    OutcomeSuperSetDto outcomeSuperSetDto = new OutcomeSuperSetDto();
    CeDetailsDto ceDetailsDto = new CeDetailsDto();
    ceDetailsDto.setEstablishmentType("TRANSIENT PERSONS");
    ceDetailsDto.setEstablishmentName("Not provided");
    outcomeSuperSetDto.setAddress(createAddress());
    outcomeSuperSetDto.setCaseId(UUID.randomUUID());
    outcomeSuperSetDto.setOutcomeCode("Test");
    List<CareCodeDto> careCodeDto = new ArrayList<>();
    careCodeDto.add(CareCodeDto.builder().careCode("Test123").build());
    outcomeSuperSetDto.setCareCodes(careCodeDto);
    outcomeSuperSetDto.setAccessInfo("12345");
    outcomeSuperSetDto.setOfficerId("Test");
    outcomeSuperSetDto.setTransactionId(UUID.randomUUID());
    outcomeSuperSetDto.setCeDetails(ceDetailsDto);
    outcomeSuperSetDto.setCoordinatorId("Test123");
    return outcomeSuperSetDto;
  }
}
