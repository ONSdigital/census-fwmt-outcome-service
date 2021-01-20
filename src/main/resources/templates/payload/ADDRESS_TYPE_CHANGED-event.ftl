"addressTypeChange":{
"newCaseId":"${newCaseId}",
"collectionCase":{
"id":"${caseId}",
<#if surveyType != "HH">
    "ceExpectedCapacity":"${usualResidents?c}",
</#if>
"address" : {
<#if surveyType != "HH">
<#if estabType??>
"estabType":"${estabType}",
</#if>
<#if estabName??>
"organisationName":"${estabName}",
</#if>
</#if>
"addressType":"${surveyType}"
}
}
}