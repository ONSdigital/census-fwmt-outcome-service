"refusal" : {
"type" : "${refusalType}",
"collectionCase" : {
"id" : "${householdOutcome.caseId}"
},
"contact": {
<#if title??>
    "title":"${title}"
</#if>
<#if forename??>
    "forename":"${forename}"
</#if>
<#if surname??>
    "surname":"${surname}"
</#if>
<#if telNo??>
    "telNo":"${telNo}"
</#if>
}
}


