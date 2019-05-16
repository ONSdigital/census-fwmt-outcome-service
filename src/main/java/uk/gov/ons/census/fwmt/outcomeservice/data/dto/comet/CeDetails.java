package uk.gov.ons.census.fwmt.outcomeservice.data.dto.comet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "establishmentName",
    "establishmentType",
    "managerTitle",
    "managerForename",
    "managerSurname",
    "usualResidents",
    "contactPhone",
    "accessInfo",
    "careCodes"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeDetails {

  @JsonProperty("establishmentName")
  private String establishmentName;

  @JsonProperty("establishmentType")
  private String establishmentType;

  @JsonProperty("managerTitle")
  private String managerTitle;

  @JsonProperty("managerForename")
  private String managerForename;

  @JsonProperty("managerSurname")
  private String managerSurname;

  @JsonProperty("usualResidents")
  private Integer usualResidents;

  @JsonProperty("contactPhone")
  private String contactPhone;

  @JsonProperty("accessInfo")
  private String accessInfo;

  @JsonProperty("careCodes")
  private List<CareCode> careCodes = null;

}
