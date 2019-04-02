package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reason",
    "collectionCase"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidAddress {

  @JsonProperty("reason")
  private ReasonEnum reason;
  @JsonProperty("collectionCase")
  private CollectionCase collectionCase;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  public enum ReasonEnum {

    DERELICT("derelict");

    private String reason;

    ReasonEnum(String reason) {
      this.reason = reason;
    }
  }
}
