"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if estabType == "CE">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if estabType == "CE">
    <#if spgOutcome.ceDetails??>
    "estabType":"${spgOutcome.ceDetails.establishmentType}",
    "organisationName":"${spgOutcome.ceDetails.establishmentName}",
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