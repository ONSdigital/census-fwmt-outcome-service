package uk.gov.ons.census.fwmt.outcomeservice.helper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.ons.census.fwmt.common.data.comet.CeDetails;
import uk.gov.ons.census.fwmt.common.data.comet.FulfillmentRequest;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;

public class HouseholdOutcomeBuilder {

  public static HouseholdOutcome createHouseholdOutcomeForPaperH() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("H1");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Paper H Questionnaire required by post");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForPaperHC() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("HC1");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Paper HC Questionnaire required by post");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForPaperI() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("I1");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Paper I Questionnaire required by post");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForPaperWithIncorrectQuestionnaireType() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("IncorrectTypeID");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Paper I Questionnaire required by post");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForHUACTextBack() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("HUAC1");
    fulfillmentRequest.setRequesterPhone("07123456789");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("HUAC required by text");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForIUACTextBack() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("IUAC1");
    fulfillmentRequest.setRequesterPhone("07123456789");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("IUAC required by text");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForIUACTextBackWithNoNumber() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("IUAC1");
    fulfillmentRequest.setRequesterPhone("");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("IUAC required by text");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForIUACTextBackWithIncorrectQuestionnaireType() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequest.setQuestionnaireType("IncorrectTypeID");
    fulfillmentRequest.setRequesterPhone("07341667945");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("IUAC required by text");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeForWillComplete() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Will Complete");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createHouseholdOutcomeWithNoSecondaryRequest() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    List<FulfillmentRequest> fulfillmentRequestList = new ArrayList<>();
    FulfillmentRequest fulfillmentRequest = new FulfillmentRequest();

    //create fulfillment's here
    fulfillmentRequest.setQuestionnaireId("QuestionnaireID");
    fulfillmentRequestList.add(fulfillmentRequest);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setFulfillmentRequests(fulfillmentRequestList);

    return householdOutcome;
  }

  public static HouseholdOutcome createContactMadeHouseholdHardRefusal() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Hard Refusal");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createContactMadeHouseholdSplitAddress() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Split Address");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createContactMadeHouseholdIncorrectSecondaryRequest() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("Contact Made");
    householdOutcome.setSecondaryOutcome("Will Complete");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createNoValidHouseholdDerelict() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("No Valid Household");
    householdOutcome.setSecondaryOutcome("Derelict");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createNoValidHouseholdIncorrectSecondaryRequest() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("No Valid Household");
    householdOutcome.setSecondaryOutcome("Will Complete");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createNoValidHouseholdIncorrectPrimaryRequest() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("IncorrectPrimaryOutcome");
    householdOutcome.setSecondaryOutcome("Will Complete");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));

    return householdOutcome;
  }

  public static HouseholdOutcome createCENoValidAddressContactMade() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();
    CeDetails ceDetails = new CeDetails();

    ceDetails.setEstablishmentName("Test house");
    ceDetails.setEstablishmentType("Test");
    ceDetails.setManagerTitle("Mr");
    ceDetails.setManagerForename("John");
    ceDetails.setManagerSurname("Smith");
    ceDetails.setContactPhone("07123456789");
    ceDetails.setUsualResidents(5);

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("No Valid Household");
    householdOutcome.setSecondaryOutcome("Property is a CE - Contact made");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setCeDetails(ceDetails);

    return householdOutcome;
  }

  public static HouseholdOutcome createCENoValidAddressNoContactMade() {

    HouseholdOutcome householdOutcome = new HouseholdOutcome();
    CeDetails ceDetails = new CeDetails();

    ceDetails.setEstablishmentName("Test house");
    ceDetails.setEstablishmentType("Test");

    householdOutcome.setEventDate(LocalDateTime.parse("2007-12-03T10:15:30"));
    householdOutcome.setUsername("Username");
    householdOutcome.setCaseId(UUID.fromString("6c9b1177-3e03-4060-b6db-f6a8456292ef"));
    householdOutcome.setCaseReference("caseReference");
    householdOutcome.setPrimaryOutcome("No Valid Household");
    householdOutcome.setSecondaryOutcome("Property is a CE - no contact made");
    householdOutcome.setTransactionId(UUID.fromString("45de4dc-3c3b-11e9-b210-d663bd873d93"));
    householdOutcome.setCeDetails(ceDetails);

    return householdOutcome;
  }
  
//  public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
//    ObjectMapper mapper = new ObjectMapper();  
//    
//    HouseholdOutcome householdOutcome1 = createHouseholdOutcomeForPaperH();
//      String ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome1);
//      System.out.println(ho);
//      
//      
//      HouseholdOutcome householdOutcome2 = createHouseholdOutcomeForPaperHC();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome2);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome3 = createHouseholdOutcomeForPaperI();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome3);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome4 = createHouseholdOutcomeForPaperWithIncorrectQuestionnaireType();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome4);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome5 = createHouseholdOutcomeForHUACTextBack();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome5);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome6 = createHouseholdOutcomeForIUACTextBack();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome6);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome7 = createHouseholdOutcomeForIUACTextBackWithNoNumber();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome7);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome8 = createHouseholdOutcomeForIUACTextBackWithIncorrectQuestionnaireType();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome8);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome9 = createHouseholdOutcomeForWillComplete();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome9);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome10 = createHouseholdOutcomeWithNoSecondaryRequest();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome10);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome11 = createContactMadeHouseholdHardRefusal();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome11);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome12 = createContactMadeHouseholdSplitAddress();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome12);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome13 = createContactMadeHouseholdIncorrectSecondaryRequest();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome13);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome14 = createNoValidHouseholdDerelict();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome14);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome15 = createNoValidHouseholdIncorrectSecondaryRequest();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome15);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome16 = createNoValidHouseholdIncorrectPrimaryRequest();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome16);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome17 = createCENoValidAddressContactMade();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome17);
//      System.out.println(ho);
//      
//      HouseholdOutcome householdOutcome18 = createCENoValidAddressNoContactMade();
//      ho = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(householdOutcome18);
//      System.out.println(ho);
//      
//  }
  
}
