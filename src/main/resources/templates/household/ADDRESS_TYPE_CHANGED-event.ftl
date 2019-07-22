"collectionCase" : {
"id":"${householdOutcome.caseId}",
<#if usualResidents gt 0>
    "numberOfResidents”:"${usualResidents}",
</#if>
"address" : {
"addressType":"${estabType}",
"estabType":"${householdOutcome.ceDetails.establishmentType}",
"orgName":"${householdOutcome.ceDetails.establishmentName}"
}
<#if secondaryOutcome == "CE - Contact made">
    ,
    "contact" : {
    "title":"${householdOutcome.ceDetails.managerTitle}",
    "forename":"${householdOutcome.ceDetails.managerForename}",
    "surname":"${householdOutcome.ceDetails.managerSurname}",
    "telNo":"${householdOutcome.ceDetails.contactPhone}"
    }
</#if>
}