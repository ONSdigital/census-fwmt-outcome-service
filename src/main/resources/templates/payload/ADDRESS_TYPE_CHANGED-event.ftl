"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if estabType != "HH">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if estabType != "HH">
    <#if !outcome.ceDetails??>
        "estabType":"Unknown",
        "organisationName":"Not Provided",
    <#elseif outcome.ceDetails??>
        <#if outcome.ceDetails.establishmentType??>
            "estabType":"${outcome.ceDetails.establishmentType}",
        <#else>
             "estabType":"Unknown",
        </#if>
        <#if outcome.ceDetails.establishmentName??>
            "organisationName":"${outcome.ceDetails.establishmentName}",
        <#else>
            "organisationName":"Not Provided",
        </#if>
    </#if>
</#if>
<#if estabType == "HH">
    "region":"${region}",
</#if>

    "addressType":"${estabType}"
}
}
}