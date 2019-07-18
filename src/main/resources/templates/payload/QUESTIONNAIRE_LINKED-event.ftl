"uac" : {
"questionnaireId":"${questionnaireId}",
"caseId":"${householdOutcome.caseId}"
}
<#if surveyType == "CCS">
"completedQuestionnaire":{
"type":"${questionnaireCompleted}",
"agentId":"${ccsPropertyListingOutcome}"
}
</#if>

