package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eventDate",
    "username",
    "caseId",
    "caseReference",
    "primaryOutcome",
    "secondaryOutcome",
    "outcomeNote",
    "ceDetails",
    "fulfillmentRequests"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdOutcome {

  @JsonProperty("eventDate")
  private LocalDateTime eventDate;

  @JsonProperty("username")
  private String username;
  
  @JsonProperty("caseId")
  private String caseId;
  
  @JsonProperty("caseReference")
  private String caseReference;
  
  @JsonProperty("primaryOutcome")
  private String primaryOutcome;
  
  @JsonProperty("secondaryOutcome")
  private String secondaryOutcome;
  
  @JsonProperty("outcomeNote")
  private String outcomeNote;
  
  @JsonProperty("transactionId")
  private UUID transactionId;
  
  @JsonProperty("ceDetails")
  private CeDetails ceDetails;
  
  @JsonProperty("fulfillmentRequests")
  private List<FulfillmentRequest> fulfillmentRequests = null;
  
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();
}
