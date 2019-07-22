package uk.gov.ons.census.fwmt.outcomeservice.util;

import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;

public final class CcsUtilityMethods {

  public static String getAddressType(CCSPropertyListingOutcome ccsPropertyListingOutcome) {

    switch (ccsPropertyListingOutcome.getPrimaryOutcome()) {
    case "Household":
      return "HH";

    case "CE":
      return "CE";

    case "Non residential or business":
      return "NR";
    }
    return "";
  }

  public static String getAddressLevel(CCSPropertyListingOutcome ccsPropertyListingOutcome) {

    if (ccsPropertyListingOutcome.getPrimaryOutcome().equals("Household")) {
      return "U";
    } else if (ccsPropertyListingOutcome.getPrimaryOutcome().equals("CE")) {
      return "E";
    }
    return "";
  }

  public static String getOrganisationName(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    if(!StringUtils.isEmpty(ccsPropertyListingOutcome.getCeDetails().getManagerName())) {
      return ccsPropertyListingOutcome.getCeDetails().getManagerName();
    }
    return "";
  }

}
