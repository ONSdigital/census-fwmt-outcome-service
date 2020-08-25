package uk.gov.ons.census.fwmt.outcomeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeDetailsDto {
  private String establishmentName;
  private String establishmentType;
  private String establishmentSecure;
  private String managerTitle;
  private String managerForename;
  private String managerSurname;
  private Integer usualResidents;
  private String contactPhone;
  private String accessInfo;
  private List<CareCodeDto> careCodes;
}