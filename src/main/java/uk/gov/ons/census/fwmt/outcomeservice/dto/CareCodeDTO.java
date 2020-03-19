package uk.gov.ons.census.fwmt.outcomeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CareCodeDTO {
  private String careCode;
}
