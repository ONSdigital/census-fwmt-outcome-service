"fulfilmentRequest" : {
"fulfilmentCode": "${productCodeLookup}",
"caseId" : "${householdOutcome.caseId}",
"address": {},
"contact": {
<#if title??>
    "title":"${title}",
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
    "telNo":"${telNo}"
<#else>
    "telNo":null
</#if>
}
<#if householdIndicator != 1>
    ,
    "individualCaseId": "${individualCaseId}"
</#if>
}
