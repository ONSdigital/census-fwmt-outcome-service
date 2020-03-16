"refusal" : {
"type": "${refusalType}",
"collectionCase": {
"id": "${householdOutcome.caseId}"
},
"contact": {
<#if title??>
    "title": "${title}",
<#else>
    "title":null,
</#if>
<#if forename??>
    "forename":"${forename}",
<#else>
    "forename":null,
</#if>
<#if surname??>
    "surname":"${surname}",
<#else>
    "surname":null,
</#if>
<#if telNo??>
    "telNo": "${telNo}"
<#else>
    "telNo": null
</#if>
}	
}


