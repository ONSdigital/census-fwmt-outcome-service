"newAddress" : {
<#if sourceCase == "NEW_UNIT">
  "sourceCaseId": "${spgOutcome.siteCaseId}",
</#if>
"collectionCase" : {
   "id" : "${newCaseId}",
    "caseType" : "SPG",
    "survey" : "CENSUS",
    "fieldcoordinatorId" : "${spgOutcome.coordinatorId}",
    "fieldofficerId" : "${officerId}",

<#if collectionCaseId??>
    "collectionExerciseId" : "${collectionCaseId}",
<#else>
</#if>
    "address" : {
      "addressLine1" : "${address.addressLine1}",
<#if sourceCase == "NEW_STANDALONE">
      "addressLine2" : "${address.addressLine2}",
      "addressLine3": "${address.addressLine3}",
      "townName" : "${address.locality}",
      "postcode" : "${address.postCode}",
      "latitude" : "${address.latitude}",
      "longitude" : "${address.longitude}",
</#if>
      "addressType" : "SPG",
      "addressLevel" : "U"
    }
  }
}