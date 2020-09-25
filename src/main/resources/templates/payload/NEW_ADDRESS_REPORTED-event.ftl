"newAddress" : {
<#if sourceCase != "NEW_STANDALONE">
"sourceCaseId" : "${sourceCaseId}",
<#else>
</#if>
"collectionCase" : {
   "id" : "${newCaseId}",
    "caseType" : "${addressType}",
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
    <#if sourceCase != "NEW_UNIT">
      "latitude" : "${address.latitude?string["0.########"]}",
      "longitude" : "${address.longitude?string["0.#########"]}",
      <#if addressType == "CE">
      "estabType": "${ceDetails.establishmentType}",
      "secureType": "${ceDetails.establishmentSecure}",
      </#if>
    </#if>
    <#if sourceCase != "NEW_SPLIT_ADDRESS">
      "addressLevel" : "${addressLevel}",
      "addressType" : "${addressType}",
    </#if>
      "region" : "${region}"
}
    <#if usualResidents??>
    ,"ceExpectedCapacity" : "${ceDetails.usualResidents}"
    </#if>
}
}