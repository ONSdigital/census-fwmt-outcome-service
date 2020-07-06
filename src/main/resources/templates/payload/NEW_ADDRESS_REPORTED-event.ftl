"newAddress" : {
<#if sourceCase == "NEW_UNIT">
"sourceCaseId" : "${outcome.siteCaseId}",
</#if>
"collectionCase" : {
   "id" : "${newCaseId}",
    "caseType" : "SPG",
    "survey" : "CENSUS",
    "fieldCoordinatorId" : "${outcome.coordinatorId}",
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
      "latitude" : "${address.latitude?string["0.########"]}",
      "longitude" : "${address.longitude?string["0.#########"]}",
      "region" : "${region}",
      "addressType" : "SPG",
      "addressLevel" : "U"
    }
  }
}