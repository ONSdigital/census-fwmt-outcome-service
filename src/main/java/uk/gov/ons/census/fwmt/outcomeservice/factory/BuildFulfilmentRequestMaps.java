package uk.gov.ons.census.fwmt.outcomeservice.factory;

import org.springframework.stereotype.Component;

@Component
public class BuildFulfilmentRequestMaps {

  private FulfilmentRequestDTO fulfilmentRequestDTO = new FulfilmentRequestDTO();

  public void buildHouseholdPaperRequestMap() {
    FulfilmentRequestFactory.householdPaperMap
        .put(fulfilmentRequestDTO.getHouseholdPaperRequestedEnglish(),
            fulfilmentRequestDTO.getHouseholdPaperRequestedEnglishPackCode());
    FulfilmentRequestFactory.householdPaperMap
        .put(fulfilmentRequestDTO.getHouseholdPaperRequestedEnglishWelshHeader(),
            fulfilmentRequestDTO.getHouseholdPaperRequestedEnglishWelshPackCode());
    FulfilmentRequestFactory.householdPaperMap
        .put(fulfilmentRequestDTO.getHouseholdPaperRequestedWelshWelshHeader(),
            fulfilmentRequestDTO.getHouseholdPaperRequestedWelshWelshPackCode());
    FulfilmentRequestFactory.householdPaperMap
        .put(fulfilmentRequestDTO.getHouseholdPaperRequestedEnglishNiHeader(),
            fulfilmentRequestDTO.getHouseholdPaperRequestedEnglishNiPackCode());
  }

  public void buildHouseholdContinuationPaperRequestMap() {
    FulfilmentRequestFactory.householdContinuationMap
        .put(fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedEnglish(),
            fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedEnglishPackCode());
    FulfilmentRequestFactory.householdContinuationMap
        .put(fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedEnglishWelshHeader(),
            fulfilmentRequestDTO
                .getHouseholdContinuationPaperRequestedEnglishWelshPackCode());
    FulfilmentRequestFactory.householdContinuationMap
        .put(fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedWelshWelshHeader(),
            fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedWelshWelshPackCode());
    FulfilmentRequestFactory.householdContinuationMap
        .put(fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedEnglishNiHeader(),
            fulfilmentRequestDTO.getHouseholdContinuationPaperRequestedEnglishNiPackCode());
  }

  public void buildIndividualPaperRequestMap() {
    FulfilmentRequestFactory.householdIndividualMap
        .put(fulfilmentRequestDTO.getIndividualPaperRequestedEnglish(),
            fulfilmentRequestDTO.getIndividualPaperRequestedEnglishPackCode());
    FulfilmentRequestFactory.householdIndividualMap
        .put(fulfilmentRequestDTO.getIndividualPaperRequestedEnglishWelshHeader(),
            fulfilmentRequestDTO.getIndividualPaperRequestedEnglishWelshPackCode());
    FulfilmentRequestFactory.householdIndividualMap
        .put(fulfilmentRequestDTO.getIndividualPaperRequestedWelshWelshHeader(),
            fulfilmentRequestDTO.getIndividualPaperRequestedWelshWelshPackCode());
    FulfilmentRequestFactory.householdIndividualMap
        .put(fulfilmentRequestDTO.getIndividualPaperRequestedEnglishNiHeader(),
            fulfilmentRequestDTO.getIndividualPaperRequestedEnglishNiPackCode());
  }

  public void buildHouseholdUacRequestMap() {
    FulfilmentRequestFactory.householdUacMap
        .put(fulfilmentRequestDTO.getHouseholdUacRequestedEnglishHeader(),
            fulfilmentRequestDTO.getHouseholdUacRequestedEnglishPackCode());
    FulfilmentRequestFactory.householdUacMap
        .put(fulfilmentRequestDTO.getHouseholdUacRequestedEnglishWelshHeader(),
            fulfilmentRequestDTO.getHouseholdUacRequestedEnglishWelshPackCode());
    FulfilmentRequestFactory.householdUacMap
        .put(fulfilmentRequestDTO.getHouseholdUacRequestedWelshWelshHeader(),
            fulfilmentRequestDTO.getHouseholdUacRequestedWelshWelshPackCode());
    FulfilmentRequestFactory.householdUacMap
        .put(fulfilmentRequestDTO.getHouseholdUacRequestedEnglishNiHeader(),
            fulfilmentRequestDTO.getHouseholdUacRequestedEnglishNiPackCode());
  }

  public void buildIndividualUacRequestMap() {
    FulfilmentRequestFactory.individualUacMap
        .put(fulfilmentRequestDTO.getIndividualUacRequestedEnglishHeader(),
            fulfilmentRequestDTO.getIndividualUacRequestedEnglishPackCode());
    FulfilmentRequestFactory.individualUacMap
        .put(fulfilmentRequestDTO.getIndividualUacRequestedEnglishWelshHeader(),
            fulfilmentRequestDTO.getIndividualUacRequestedEnglishWelshPackCode());
    FulfilmentRequestFactory.individualUacMap
        .put(fulfilmentRequestDTO.getIndividualUacRequestedWelshWelshHeader(),
            fulfilmentRequestDTO.getIndividualUacRequestedWelshWelshPackCode());
    FulfilmentRequestFactory.individualUacMap
        .put(fulfilmentRequestDTO.getIndividualUacRequestedEnglishNiHeader(),
            fulfilmentRequestDTO.getIndividualUacRequestedEnglishNiPackCode());
  }
}