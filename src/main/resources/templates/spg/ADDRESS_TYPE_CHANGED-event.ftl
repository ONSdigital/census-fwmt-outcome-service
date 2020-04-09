"addressTypeChange" : {
"collectionCase": {
"id": "${caseId}",
<#if estabType == "CE">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if estabType == "CE">
    "estabType":"${spgOutcome.ceDetails.establishmentType}",
    "organisationName": "${spgOutcome.ceDetails.establishmentName}",
</#if>
    "addressType":"${estabType}"
}
}
}