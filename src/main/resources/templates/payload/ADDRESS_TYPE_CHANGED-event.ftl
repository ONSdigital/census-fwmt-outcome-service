"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if estabType == "CE">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if estabType == "CE">
    <#if outcome.ceDetails??>
    "estabType":"${outcome.ceDetails.establishmentType}",
    "organisationName":"${outcome.ceDetails.establishmentName}",
    <#else>
    "organisationName":"Unknown",
    "estabType":"Unknown",
    </#if>
</#if>
    "region":"${region}",
    "addressType":"${estabType}"
}
}
}