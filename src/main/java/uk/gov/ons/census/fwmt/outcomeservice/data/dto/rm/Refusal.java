package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "report",
    "agentId",
    "collectionCase"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refusal {

  @JsonProperty("type")
  private String type;

  @JsonProperty("report")
  private String report;

  @JsonProperty("agentId")
  private String agentId;

  @JsonProperty("collectionCase")
  private CollectionCase collectionCase;
}
