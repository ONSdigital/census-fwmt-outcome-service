"addressTypeChange" : {
"collectionCase": {
"id": "${caseId}",
<#if usualResidents gt 0>
    "ceExpectedCapacity”:"${usualResidents}",
</#if>
"address" : {
"addressType":"${estabType}",
<#if estabType == "CE">
    "estabType":"${householdOutcome.ceDetails.establishmentType}",
    "organisationName": "${householdOutcome.ceDetails.establishmentName}"
<#else>
</#if>
}
}
}