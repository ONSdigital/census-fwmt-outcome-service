"fulfilmentRequest" : {
"fulfilmentCode": "${productCodeLookup}",
"caseId" : "${householdOutcome.caseId}",
"address": {},
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
<#if householdIndicator != 1>
    ,
    "individualCaseId": "${individualCaseId}"
</#if>
}
