package uk.gov.ons.census.fwmt.outcomeservice.converter;

import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface SPGOutcomeServiceProcessor {

  void processMessageSpgOutcome(SPGOutcome spgOutcome) throws GatewayException;

  void processMessageNewUnitAddress(NewUnitAddress newUnitAddress) throws GatewayException;

  void processMessageNewStandaloneAddress(NewStandaloneAddress newStandaloneAddress) throws GatewayException;

}
