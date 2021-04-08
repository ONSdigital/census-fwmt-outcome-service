package uk.gov.ons.census.fwmt.outcomeservice.util;

import uk.gov.ons.census.fwmt.outcomeservice.dto.FulfilmentRequestDto;
import uk.gov.ons.census.fwmt.outcomeservice.dto.OutcomeSuperSetDto;

import java.util.List;

public final class SpgUtilityMethods {

  public static boolean isDelivered(OutcomeSuperSetDto outcome) {
    List<FulfilmentRequestDto> fulfilmentRequestList = outcome.getFulfilmentRequests();
    if (fulfilmentRequestList == null) return false;
    boolean isDelivered = false;
    for (FulfilmentRequestDto fulfilmentRequest : fulfilmentRequestList) {
      if (fulfilmentRequest.getQuestionnaireID() != null) {
        isDelivered = true;
        break;
      }
    }
    return isDelivered;
  }

  public static String regionLookup(String officerId) {
    if (officerId.substring(1,2).equals("W")) {
      return "W";
    } else if (officerId.substring(0,1).equals("Y")) {
      return "N";
    } else {
      return "E";
    }
  }
}
