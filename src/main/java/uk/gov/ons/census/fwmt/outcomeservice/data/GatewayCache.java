package uk.gov.ons.census.fwmt.outcomeservice.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gateway_cache")
public class GatewayCache {
  @Id
  @Column(name = "case_id", unique = true, nullable = false)
  public String caseId;

  @Column(name = "exists_in_fwmt")
  @JsonProperty("existsInFWMT")
  public boolean existsInFwmt;

  @Column(name = "is_delivered")
  public boolean delivered;

  @Column(name = "care_code")
  public String careCodes;

  @Column(name = "access_info")
  public String accessInfo;

  @Column(name = "uprn")
  public String uprn;

  @Column(name = "estab_uprn")
  public String estabUprn;

  @Column(name ="type")
  public Integer type;

  @Column(name = "individual_case_id")
  public String individualCaseId;

  @Column(name ="oa")
  public String oa;

  @Column(name = "manager_title")
  public String managerTitle;

  @Column(name = "manager_firstname")
  public String managerFirstname;

  @Column(name = "manager_surname")
  public String managerSurname;

  @Column(name = "manager_number")
  public String managerContactNumber;

  @Column(name = "usual_residents")
  public Integer usualResidents;

  @Column(name = "bedspaces")
  public Integer bedspaces;

  @Column(name = "original_case_id")
  public String originalCaseId;

  // TODO : Probably move this object to common so its in one location

}
