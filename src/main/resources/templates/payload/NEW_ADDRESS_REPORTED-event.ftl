"newAddress" : {
<#if sourceCase != "NEW_STANDALONE">
"sourceCaseId" : "${sourceCaseId}",
<#else>
</#if>
"collectionCase" : {
   "id" : "${newCaseId}",
    "caseType" : "${surveyType}",
    "survey" : "CENSUS",
    "fieldCoordinatorId" : "${outcome.coordinatorId}",
    "fieldOfficerId" : "${officerId}",
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
    <#if sourceCase == "NEW_STANDALONE">
      "latitude" : "${address.latitude?string["0.########"]}",
      "longitude" : "${address.longitude?string["0.#########"]}",
      <#if surveyType != "HH">
      "estabType": "${ceDetails.establishmentType}",
      "organisationName": "${ceDetails.establishmentName}",
      "secureType": "${ceDetails.establishmentSecure}",
      </#if>
    </#if>
    "addressLevel" : "${addressLevel}",
    "addressType" : "${surveyType}",
    "region" :"${region}"
}
    <#if usualResidents??>
    ,"ceExpectedCapacity" : "${usualResidents?c}"
    </#if>
}
}