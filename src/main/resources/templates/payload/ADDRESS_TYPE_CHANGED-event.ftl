"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if surveyType != "HH">
    "ceExpectedCapacity":"${usualResidents}",
</#if>
"address" : {
<#if surveyType != "HH">
"estabType":"${estabType}",
"organisationName":"${estabName}",
</#if>
"addressType":"${surveyType}"
}
}
}