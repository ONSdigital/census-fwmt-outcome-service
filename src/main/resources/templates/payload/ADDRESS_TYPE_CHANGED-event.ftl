"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if estabType != "HH">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if estabType != "HH">
    <#if outcome.ceDetails??>
    "estabType":"${outcome.ceDetails.establishmentType}",
    "organisationName":"${outcome.ceDetails.establishmentName}",
    <#else>
    "organisationName":"Unknown",
    "estabType":"Unknown",
    </#if>
</#if>
    "addressType":"${estabType}"
}
}
}