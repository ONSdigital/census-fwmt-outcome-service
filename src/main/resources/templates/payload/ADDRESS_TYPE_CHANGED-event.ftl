"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if surveyType != "HH">
    "ceExpectedCapacity":"${usualResidents}",
    "addressType":"${estabType}",
</#if>
"address" : {
<#if surveyType != "HH">
"estabType":"${estabType}",
"organisationName":"${estabName}",
</#if>
<#if surveyType == "HH">
    "region":"${region}"
</#if>
}
}
}