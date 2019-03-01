package uk.gov.ons.census.fwmt.feedbackservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CensusCaseOutcomeDTO {
  private String caseId;
  private String caseReference;
  private String outcomeCategory;
  private String outcome;
  private String outcomeNote;
}
