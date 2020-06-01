"addressTypeChange":{
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
    "organisationName":null,
    "estabType":null,
    </#if>
</#if>
    "region":"${region}",
    "addressType":"${estabType}"
}
}
}