"fulfilment" : {
"productCode": "${productCodeLookup}",
"caseId" : "${householdOutcome.caseId}",
"address": {},
"contact": {
<#if telNo??>
"telNo":"${telNo}"
</#if>
}
<#if householdIndicator != 1>
    ,
    "individualCaseId": "${individualCaseId}"
</#if>
}
