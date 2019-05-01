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
    "invalidAddress",
    "refusal",
    "contact"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

  @JsonProperty("invalidAddress")
  private InvalidAddress invalidAddress;

  @JsonProperty("refusal")
  private Refusal refusal;

  @JsonProperty("contact")
  private Contact contact;
  
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();
}
