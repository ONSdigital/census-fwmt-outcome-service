"newAddress" : {
<#if sourceCase == "NEW_UNIT">
  "sourceCaseId" : "${spgOutcome.siteCaseId}",
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
    <#if address.addressLine2??>
      "addressLine2" : "${address.addressLine2}",
    <#else>
      "addressLine2" : null,
    </#if>
    <#if address.addressLine3??>
      "addressLine3" : "${address.addressLine3}",
    <#else>
      "addressLine3" : null,
    </#if>
    <#if address.locality??>
       "townName" : "${address.locality}",
    <#else>
       "townName" : null,
    </#if>
    <#if address.postCode??>
      "postcode" : "${address.postCode}",
    <#else>
      "postcode" : null,
    </#if>
      "latitude" : "${address.latitude}",
      "longitude" : "${address.longitude}",
</#if>
      "addressType" : "SPG",
      "addressLevel" : "U"
    }
  }
}