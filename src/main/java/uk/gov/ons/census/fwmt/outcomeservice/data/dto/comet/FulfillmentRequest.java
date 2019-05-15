package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "questionnaireType",
    "questionnaireId",
    "requesterTitle",
    "requesterForename",
    "requesterSurname",
    "requesterPhone"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfillmentRequest {

  @JsonProperty("questionnaireType")
  private String questionnaireType;

  @JsonProperty("questionnaireId")
  private String questionnaireId;

  @JsonProperty("requesterTitle")
  private String requesterTitle;

  @JsonProperty("requesterForename")
  private String requesterForename;

  @JsonProperty("requesterSurname")
  private String requesterSurname;

  @JsonProperty("requesterPhone")
  private String requesterPhone;

}
