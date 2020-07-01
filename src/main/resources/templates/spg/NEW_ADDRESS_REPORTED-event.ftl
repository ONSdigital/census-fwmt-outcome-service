"newAddress" : {
<#if sourceCase == "NEW_UNIT">
"sourceCaseId" : "${spgOutcome.siteCaseId}",
</#if>
"collectionCase" : {
   "id" : "${newCaseId}",
    "caseType" : "SPG",
    "survey" : "CENSUS",
    "fieldCoordinatorId" : "${spgOutcome.coordinatorId}",
    "fieldOfficerId" : "${officerId}",
<#if collectionCaseId??>
    "collectionExerciseId" : "${collectionCaseId}",
<#else>
</#if>
    "address" : {
      "addressLine1" : "${address.addressLine1}",
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
    <#if address.postcode??>
      "postcode" : "${address.postcode}",
    <#else>
      "postcode" : null,
    </#if>
    <#if address.latitude??>
      "latitude" : "${address.latitude?string["0.########"]}",
    <#else>
      "latitude" : null,
    </#if>
    <#if address.latitude??>
      "longitude" : "${address.longitude?string["0.#########"]}",
    <#else>
      "longitude" : null,
    </#if>
      "region" : "${region}",
      "addressType" : "SPG",
      "addressLevel" : "U"
    }
  }
}