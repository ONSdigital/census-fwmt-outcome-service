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

//  @Column(name = "manager_title")
//  public final String managerTitle;
//
//  @Column(name = "manager_firstname")
//  public final String managerFirstname;
//
//  @Column(name = "manager_surname")
//  public final String managerSurname;
//
//  @JsonProperty("contactPhoneNo")
//  @Column(name = "contact_phone_number")
//  public final String contactPhoneNumber;

}
