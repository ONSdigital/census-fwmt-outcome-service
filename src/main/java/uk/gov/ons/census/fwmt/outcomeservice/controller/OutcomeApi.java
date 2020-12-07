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
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ce.CENewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CENewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HHNewSplitAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.household.HHOutcome;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGNewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

@Api(value = "FWMT Census Outcome Service", description = "Operations pertaining to receiving outcomes from COMET")
@RestController
public interface OutcomeApi {

  @ApiOperation(value = "Post a CE survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ceOutcome/{caseID}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ceOutcomeResponse(
      @PathVariable("caseID") String caseID, @RequestBody CEOutcome ceOutcome) throws GatewayException;

  @ApiOperation(value = "Post a CE survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ceOutcome/unitAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ceNewUnitAddress(@RequestBody CENewUnitAddress ceOutcome) throws GatewayException;

  @ApiOperation(value = "Post a CE survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ceOutcome/standaloneAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ceNewStandalone(@RequestBody CENewStandaloneAddress ceOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/spgOutcome/{caseID}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgOutcomeResponse(
      @PathVariable("caseID") String caseID, @RequestBody SPGOutcome spgOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/spgOutcome/unitAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgNewUnitAddress(@RequestBody SPGNewUnitAddress spgOutcome) throws GatewayException;

  @ApiOperation(value = "Post a SPG survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/spgOutcome/standaloneAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> spgNewStandalone(@RequestBody SPGNewStandaloneAddress spgOutcome) throws GatewayException;

  @ApiOperation(value = "Post a HH survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/hhOutcome/{caseID}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> hhOutcomeResponse(
      @PathVariable("caseID") String caseID, @RequestBody HHOutcome hhOutcome) throws GatewayException;

  @ApiOperation(value = "Post a HH survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/hhOutcome/splitAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> hhNewSplitAddress(@RequestBody HHNewSplitAddress hhNewSplitAddress) throws GatewayException;

  @ApiOperation(value = "Post a HH survey outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/hhOutcome/standaloneAddress/new",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> hhNewStandalone(@RequestBody HHNewStandaloneAddress hhNewStandaloneAddress) throws GatewayException;

  @ApiOperation(value = "Post a CCS property listing outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ccsPropertyListingOutcome",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ccsPropertyListing(@RequestBody CCSPropertyListingOutcome ccsPropertyListingOutcome) throws GatewayException;

  @ApiOperation(value = "Post a CCS Interview outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ccsInterviewOutcome/{caseID}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ccsInterview(@PathVariable("caseID") String caseID, @RequestBody CCSInterviewOutcome ccsInterviewOutcome) throws GatewayException;

  @ApiOperation(value = "Post a Non-Compliance outcome to the FWMT Gateway")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Case Outcome received"),
      @ApiResponse(code = 401, message = "UNAUTHORIZED"),
      @ApiResponse(code = 403, message = "FORBIDDEN")})
  @RequestMapping(value = "/ncOutcome/{caseID}",
      produces = {"application/json"},
      method = RequestMethod.POST)
  ResponseEntity<Void> ncOutcome(@PathVariable("caseID") String caseID, @RequestBody NCOutcome ncOutcome) throws GatewayException;

}
