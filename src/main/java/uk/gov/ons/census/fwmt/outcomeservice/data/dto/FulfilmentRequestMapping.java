package uk.gov.ons.census.fwmt.outcomeservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentRequestMapping {

  // Fulfillment Request Mapping
  // Household Paper Requested
  @Value("${fulfillmentRequestMapping.householdPaperRequested.english}")
  String householdPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.english.packCode}")
  String householdPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishWelshHeader}")
  String householdPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishWelshHeader.packCode}")
  String householdPaperRequestedEnglishWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.welshWelshHeader}")
  String householdPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.welshWelshHeader.packCode}")
  String householdPaperRequestedWelshWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishNiHeader}")
  String householdPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishNiHeader.packCode}")
  String householdPaperRequestedEnglishNiHeaderPackCode;

  // Household UAC Requested
  @Value("${fulfillmentRequestMapping.householdUacRequested}")
  String householdUacRequested;

  @Value("${fulfillmentRequestMapping.householdUacRequested.packCode}")
  String householdUacRequestedPackCode;

  // Need mapping
  @Value("${fulfillmentRequestMapping.householdUacRequested.englishWelshHeader}")
  String householdUacRequestedEnglish;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishWelshHeader.packCode}")
  String householdUacRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdUacRequested.welshWelshHeader}")
  String householdUacRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.welshWelshHeader.packCode}")
  String householdUacRequestedWelshWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishNiHeader}")
  String householdUacRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishNiHeader.packCode}")
  String householdUacRequestedEnglishNiHeaderPackCode;

  // Household Continuation Paper Requested
  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.english}")
  String householdContinuationPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.english.packCode}")
  String householdContinuationPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishWelshHeader}")
  String householdContinuationPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishWelshHeader.packCode}")
  String householdContinuationPaperRequestedEnglishWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.welshWelshHeader}")
  String householdContinuationPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.welshWelshHeader.packCode}")
  String householdContinuationPaperRequestedWelshWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishNiHeader}")
  String householdContinuationPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishNiHeader.packCode}")
  String householdContinuationPaperRequestedEnglishNiHeaderPackCode;

  // Individual Paper Requested
  @Value("${fulfillmentRequestMapping.individualPaperRequested.english}")
  String individualPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.packCode}")
  String individualPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english}")
  String individualPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.packCode}")
  String individualPaperRequestedEnglishWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english}")
  String individualPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.packCode}")
  String individualPaperRequestedWelshWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english}")
  String individualPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.packCode}")
  String individualPaperRequestedEnglishNiHeaderPackCode;

  // Individual UAC Requested
  @Value("${fulfillmentRequestMapping.individualUacRequested}")
  String individualUacRequested;

  // Need mapping
  @Value("${fulfillmentRequestMapping.individualUacRequested.englishWelshHeader}")
  String individualUacRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishWelshHeader.packCode}")
  String individualUacRequestedEnglishWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.individualUacRequested.welshWelshHeader}")
  String individualUacRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.welshWelshHeader.packCode}")
  String individualUacRequestedWelshWelshHeaderPackCode;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishNiHeader}")
  String individualUacRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishNiHeader.packCode}")
  String individualUacRequestedEnglishNiHeaderPackCode;

}
