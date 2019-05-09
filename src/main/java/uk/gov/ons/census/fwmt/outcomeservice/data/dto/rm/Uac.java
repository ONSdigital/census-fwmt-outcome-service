package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "questionnaireId",
    "caseId"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Uac {

  @JsonProperty("questionnaireId")
  private String questionnaireId;

  @JsonProperty("caseId")
  private UUID caseId;

}
