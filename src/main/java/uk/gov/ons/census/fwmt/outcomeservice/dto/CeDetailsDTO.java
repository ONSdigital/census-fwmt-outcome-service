package uk.gov.ons.census.fwmt.outcomeservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeDetailsDTO {
  private String establishmentName;
  private String establishmentType;
  private String managerTitle;
  private String managerForename;
  private String managerSurname;
  private Integer usualResidents;
  private String contactPhone;
  private String accessInfo;
  private List<CareCodeDTO> careCodes;

}