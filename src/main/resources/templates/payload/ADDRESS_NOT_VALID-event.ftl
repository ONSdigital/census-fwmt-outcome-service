"InvalidAddress":{
"reason":"${secondaryOutcome}",
<#if surveyType == "Household">
"collectionCase":{
"id":"${householdOutcome.caseId}"
}
</#if>
}
