package uk.gov.ons.census.fwmt.outcomeservice.util;

import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;

public final class CcsUtilityMethods {

  private final static String HOUSEHOLD = "Household";
  private final static String NON_RES = "Non residential or business";
  private final static String HH = "HH";
  private final static String CE = "CE";
  private final static String NR = "NR";
  private final static String U = "U";
  private final static String E = "E";


  public static String getAddressType(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    switch (ccsPropertyListingOutcome.getPrimaryOutcome()) {
    case HOUSEHOLD:
      return HH;
    case CE:
      return CE;
    case NON_RES:
      return NR;
    }
    return "";
  }

  public static String getAddressLevel(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    switch (ccsPropertyListingOutcome.getPrimaryOutcome()) {
    case HOUSEHOLD:
    case NON_RES:
      return U;
    case CE:
      return E;
    }
    return "";
  }

  public static String getOrganisationName(CCSPropertyListingOutcome ccsPropertyListingOutcome) {
    if (ccsPropertyListingOutcome.getCeDetails() != null) {
      if (!StringUtils.isEmpty(ccsPropertyListingOutcome.getCeDetails().getOrganisationName())) {
        return ccsPropertyListingOutcome.getCeDetails().getOrganisationName();
      } 
    }
    return "";
  }
}
