"addressTypeChange" : {
"collectionCase" : {
"id":"${generatedCaseId}",
<#if usualResidents gt 0>
    "numberOfResidents”:"${usualResidents}",
</#if>
"address" : {
"addressType":"CE",
"estabType":"${householdOutcome.ceDetails.establishmentType}",
<#if householdOutcome.ceDetails.establishmentName??>
    "orgName":"${householdOutcome.ceDetails.establishmentName}"
<#else>
    "orgName":null
</#if>
}
}
}