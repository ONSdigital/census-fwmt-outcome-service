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
    "questionnaireType",
    "deliveryFormat",
    "deliveryMethod",
    "questionnaireId",
    "requesterName",
    "requesterPhone"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfillmentRequest {

  @JsonProperty("questionnaireType")
  private String questionnaireType;
  
  @JsonProperty("deliveryFormat")
  private String deliveryFormat;
  
  @JsonProperty("deliveryMethod")
  private String deliveryMethod;
  
  @JsonProperty("questionnaireId")
  private String questionnaireId;
  
  @JsonProperty("requesterName")
  private String requesterName;
  
  @JsonProperty("requesterPhone")
  private String requesterPhone;
  
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

}
