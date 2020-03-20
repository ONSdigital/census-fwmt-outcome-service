package uk.gov.ons.census.fwmt.outcomeservice.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.census.fwmt.common.data.spg.Address;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SPGOutcomeSuperSetDTO {
  private UUID caseId;
  private UUID transactionId;
  private Date eventDate;
  private String officerId;
  private String coordinatorId;
  private String primaryOutcomeDescription;
  private String secondaryOutcomeDescription;
  private String outcomeCode;
  private Address address;
  private String accessInfo;
  private List<CareCodeDTO> careCodes;
  private List<FulfilmentRequestDTO> fulfillmentRequests;
  private String accompanyingOfficerId;
  private CeDetailsDTO ceDetails;
  private UUID siteCaseId;
  private Boolean dummyInfoCollected;
}
