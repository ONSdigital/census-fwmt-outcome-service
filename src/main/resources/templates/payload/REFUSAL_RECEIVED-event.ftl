"refusal" : {
"type" : "${refusalType}"
<#if surveyType == "CCS">
"report":"${report},
"agentId":"${agentId}"
</#if>
}
<#if surveyType == "Household">
,
"collectionCase" : {
"id" : "${householdOutcome.caseId}"
}
</#if>

