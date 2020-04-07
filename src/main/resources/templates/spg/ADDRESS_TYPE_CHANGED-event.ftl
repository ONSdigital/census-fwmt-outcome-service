"addressTypeChange" : {
"collectionCase": {
"id": "${caseId}",
<#if estabType == "CE">
    "ceExpectedCapacity”:"${usualResidents}",
</#if>
"address" : {
    "addressType":"${estabType}",
<#if estabType == "CE">
    "estabType":"${householdOutcome.ceDetails.establishmentType}",
    "organisationName": "${householdOutcome.ceDetails.establishmentName}"
</#if>
}
}
}