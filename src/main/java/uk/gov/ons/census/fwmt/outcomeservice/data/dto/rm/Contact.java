package uk.gov.ons.census.fwmt.outcomeservice.data.dto.rm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "forename",
    "surname",
    "email",
    "telNo"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

  @JsonProperty("title")
  private String title;

  @JsonProperty("forename")
  private String forename;

  @JsonProperty("surname")
  private String surname;

  @JsonProperty("email")
  private String email;

  @JsonProperty("telNo")
  private String telNo;

}
