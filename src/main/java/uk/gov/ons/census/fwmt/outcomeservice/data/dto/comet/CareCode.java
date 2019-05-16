package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "careCode"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareCode {

  @JsonProperty("careCode")
  private String careCode;

}
