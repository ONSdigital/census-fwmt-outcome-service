{
    "collectionCase" : {
    "id":"${householdOutcome.caseId}",
    <#if usualSuspects gt 0 >
    "numberOfResidents”:"${usualSuspects}",
    </#if>
    "address" : {
        "addressType":"${householdOutcome.ceDetails.usualResidents}",
        "estabType":"${estabType}",
        "orgName":"${householdOutcome.ceDetails.establishmentName}",
    },
    <#if householdOutcome.SecondaryOutcome = "Property is a CE - Contact made" >
    "contact" : {
         "title":"${householdOutcome.ceDetails.managerTitle}",
         "forename":"${householdOutcome.ceDetails.managerForename}",
         "surname":"${householdOutcome.ceDetails.managerSurname}",
         "telNo":"${householdOutcome.ceDetails.contactPhone}"
    }
    </#if>
}