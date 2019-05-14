package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactionId",
    "eventDate",
    "username",
    "caseId",
    "caseReference",
    "primaryOutcome",
    "secondaryOutcome",
    "ceDetails",
    "fulfilmentRequests"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdOutcome {

  @JsonProperty("transactionId")
  private UUID transactionId;

  @JsonProperty("eventDate")
  private LocalDateTime eventDate;

  @JsonProperty("username")
  private String username;

  @JsonProperty("caseId")
  private UUID caseId;

  @JsonProperty("caseReference")
  private String caseReference;

  @JsonProperty("primaryOutcome")
  private String primaryOutcome;

  @JsonProperty("secondaryOutcome")
  private String secondaryOutcome;

  @JsonProperty("ceDetails")
  private CeDetails ceDetails;

  @JsonProperty("fulfilmentRequests")
  private List<FulfilmentRequest> fulfilmentRequests = null;

}
