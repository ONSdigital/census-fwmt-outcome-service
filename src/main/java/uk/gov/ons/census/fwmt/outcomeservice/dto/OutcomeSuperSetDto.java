package uk.gov.ons.census.fwmt.outcomeservice.dto;

import lombok.Data;
import uk.gov.ons.census.fwmt.common.data.spg.Address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OutcomeSuperSetDto {
  private UUID caseId;
  private UUID transactionId;
  private Date eventDate;
  private String officerId;
  private String coordinatorId;
  private String primaryOutcomeDescription;
  private String secondaryOutcomeDescription;
  private String outcomeCode;
  private Address address;
  private String accessInfo;
  private List<CareCodeDto> careCodes;
  private List<FulfilmentRequestDto> fulfilmentRequests;
  private String accompanyingOfficerId;
  private CeDetailsDto ceDetails;
  private UUID siteCaseId;
  private Boolean dummyInfoCollected;

  public static String careCodesToText(List<CareCodeDto> careCodes) {
    if (careCodes == null) return "";
    List<String> ccs = new ArrayList<String>();
    careCodes.stream().forEach(cc -> ccs.add(cc.getCareCode()));
    return String.join(",", ccs);
  }

  public Date getEventDate() {
    return (eventDate != null) ? new Date(eventDate.getTime()) : null;
  }

  public void setEventDate(Date ed) {
    eventDate = (ed != null) ? new Date(ed.getTime()) : null;
  }
}
