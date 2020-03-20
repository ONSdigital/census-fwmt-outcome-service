package uk.gov.ons.census.fwmt.outcomeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentRequestDTO {
  private String questionnaireType;
  private String questionnaireID;
  private String requesterTitle;
  private String requesterForename;
  private String requesterSurname;
  private String requesterPhone;
}