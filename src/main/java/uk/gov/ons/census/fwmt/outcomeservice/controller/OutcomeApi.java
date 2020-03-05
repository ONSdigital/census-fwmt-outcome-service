package uk.gov.ons.census.fwmt.outcomeservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

@Api(value = "FWMT Census Outcome Service", description = "Operations pertaining to receiving outcomes from COMET")
@RestController
public interface OutcomeApi {

  //  @ApiOperation(value = "Post a household survey outcome to the FWMT Gateway")
  //  @ApiResponses(value = {
  //      @ApiResponse(code = 202, message = "Case Outcome received")})
  //  @RequestMapping(value = "/householdOutcome/{caseId}",
  //      produces = {"application/json"},
  //      method = RequestMethod.POST)
  //  ResponseEntity<Void> householdCaseOutcomeResponse(
  //      @PathVariable String caseId, @RequestBody HouseholdOutcome householdOutcome) throws GatewayException;
  //
  //  @ApiOperation(value = "Post a CCS Property Listing outcome to the FWMT Gateway")
  //  @ApiResponses(value = {
  //      @ApiResponse(code = 202, message = "Case Outcome received", response = CCSPropertyListingOutcome.class)})
  //  @RequestMapping(value = "/ccsPropertyListingOutcome",
  //      produces = {"application/json"},
  //      method = RequestMethod.POST)
  //  ResponseEntity<Void> ccsPropertyListingCaseOutcomeResponse(
  //      @RequestBody CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException;
  //
  //  @ApiOperation(value = "Post a CCS Interview outcome to the FWMT Gateway")
  //  @ApiResponses(value = {
  //      @ApiResponse(code = 202, message = "Case Outcome received")})
  //  @RequestMapping(value = "/ccsInterviewOutcome/{caseId}",
  //      produces = {"application/json"},
  //      method = RequestMethod.POST)
  //  ResponseEntity<Void> ccsInterviewOutcome(
  //      @PathVariable String caseId, @RequestBody CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received")})
  @RequestMapping(value = "/spgOutcome/{caseId}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgOutcomeResponse(
      @PathVariable String caseId, @RequestBody SPGOutcome spgOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received")})
  @RequestMapping(value = "/spgOutcome/unitAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgNewUnitAddress(
      @PathVariable String caseId, @RequestBody SPGOutcome spgOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Case Outcome received")})
  @RequestMapping(value = "/spgOutcome/unitAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgNewStandalone(
      @PathVariable String caseId, @RequestBody SPGOutcome spgOutcome) throws GatewayException;

}
