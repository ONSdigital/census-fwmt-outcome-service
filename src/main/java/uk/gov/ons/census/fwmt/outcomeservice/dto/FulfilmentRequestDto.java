package uk.gov.ons.census.fwmt.outcomeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"requesterTitle", "requesterForename", "requesterSurname", "requesterPhone"})
public class FulfilmentRequestDto {
  private String questionnaireType;
  private String questionnaireID;
  private String requesterTitle;
  private String requesterForename;
  private String requesterSurname;
  private String requesterPhone;
}