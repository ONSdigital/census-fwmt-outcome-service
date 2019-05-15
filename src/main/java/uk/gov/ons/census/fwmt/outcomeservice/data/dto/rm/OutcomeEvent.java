package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "event",
    "payload",
    "fulfillment"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutcomeEvent {

  @JsonProperty("event")
  private Event event;

  @JsonProperty("payload")
  private Payload payload;

}
