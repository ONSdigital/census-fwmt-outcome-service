package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

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
    "establishmentName",
    "managerName",
    "usualResidents",
    "mgrPhone"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeDetails {

  @JsonProperty("establishmentName")
  private String establishmentName;
  @JsonProperty("managerName")
  private String managerName;
  @JsonProperty("usualResidents")
  private Integer usualResidents;
  @JsonProperty("mgrPhone")
  private String mgrPhone;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

}
