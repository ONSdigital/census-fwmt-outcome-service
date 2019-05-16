package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactionId",
    "type",
    "source",
    "channel",
    "dateTime"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

  @JsonProperty("transactionId")
  private UUID transactionId;

  @JsonProperty("type")
  private String type;

  @JsonProperty("source")
  private String source;

  @JsonProperty("channel")
  private String channel;

  // "2011-08-12T20:17:46.384Z" example
  @JsonProperty("dateTime")
  private LocalDateTime dateTime;

}
