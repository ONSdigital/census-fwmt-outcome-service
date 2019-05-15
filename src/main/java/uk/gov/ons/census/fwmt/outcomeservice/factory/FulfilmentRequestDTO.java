package uk.gov.ons.census.fwmt.outcomeservice.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class FulfilmentRequestDTO {

  // Fulfillment Request Mapping
  // Household Paper Requested
  @Value("${fulfillmentRequestMapping.householdPaperRequested.english.header}")
  String householdPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.english.packCode}")
  String householdPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishWelsh.header}")
  String householdPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishWelsh.packCode}")
  String householdPaperRequestedEnglishWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.welshWelsh.header}")
  String householdPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.welshWelsh.packCode}")
  String householdPaperRequestedWelshWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishNi.header}")
  String householdPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdPaperRequested.englishNi.packCode}")
  String householdPaperRequestedEnglishNiPackCode;

  // Household UAC Requested
  @Value("${fulfillmentRequestMapping.householdUacRequested.english.header}")
  String householdUacRequestedEnglishHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.english.packCode}")
  String householdUacRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishWelsh.header}")
  String householdUacRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishWelsh.packCode}")
  String householdUacRequestedEnglishWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdUacRequested.welshWelsh.header}")
  String householdUacRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.welshWelsh.packCode}")
  String householdUacRequestedWelshWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishNi.header}")
  String householdUacRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdUacRequested.englishNi.packCode}")
  String householdUacRequestedEnglishNiPackCode;

  // Household Continuation Paper Requested
  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.english.header}")
  String householdContinuationPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.english.packCode}")
  String householdContinuationPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishWelsh.header}")
  String householdContinuationPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishWelsh.packCode}")
  String householdContinuationPaperRequestedEnglishWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.welshWelsh.header}")
  String householdContinuationPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.welshWelsh.packCode}")
  String householdContinuationPaperRequestedWelshWelshPackCode;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishNi.header}")
  String householdContinuationPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.householdContinuationPaperRequested.englishNi.packCode}")
  String householdContinuationPaperRequestedEnglishNiPackCode;

  // Individual Paper Requested
  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.header}")
  String individualPaperRequestedEnglish;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.english.packCode}")
  String individualPaperRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.englishWelsh.header}")
  String individualPaperRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.englishWelsh.packCode}")
  String individualPaperRequestedEnglishWelshPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.welshWelsh.header}")
  String individualPaperRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.welshWelsh.packCode}")
  String individualPaperRequestedWelshWelshPackCode;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.englishNi.header}")
  String individualPaperRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.individualPaperRequested.englishNi.packCode}")
  String individualPaperRequestedEnglishNiPackCode;

  // Individual UAC Requested
  @Value("${fulfillmentRequestMapping.individualUacRequested.english.header}")
  String individualUacRequestedEnglishHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.english.packCode}")
  String individualUacRequestedEnglishPackCode;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishWelsh.header}")
  String individualUacRequestedEnglishWelshHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishWelsh.packCode}")
  String individualUacRequestedEnglishWelshPackCode;

  @Value("${fulfillmentRequestMapping.individualUacRequested.welshWelsh.header}")
  String individualUacRequestedWelshWelshHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.welshWelsh.packCode}")
  String individualUacRequestedWelshWelshPackCode;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishNi.header}")
  String individualUacRequestedEnglishNiHeader;

  @Value("${fulfillmentRequestMapping.individualUacRequested.englishNi.packCode}")
  String individualUacRequestedEnglishNiPackCode;

}
