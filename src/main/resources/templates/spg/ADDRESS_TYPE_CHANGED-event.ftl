"addressTypeChange" : {
"collectionCase": {
"id": "${caseId}",
<#if usualResidents gt 0>
    "ceExpectedCapacity”:"${usualResidents}",
</#if>
"address" : {
"addressType":"${estabType}",
"estabType":"${householdOutcome.ceDetails.establishmentType}",
<#if estabType == "CE">
    "organizationName": "${householdOutcome.ceDetails.establishmentName}"
<#else>
    "organizationName": null
</#if>
}
}
}