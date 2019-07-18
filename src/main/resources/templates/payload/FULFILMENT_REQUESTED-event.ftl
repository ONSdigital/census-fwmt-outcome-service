"fulfilment" : {
"productCode": "${productCodeLookup}",
"caseId" : "${householdOutcome.caseId}",
"address": {},
"contact": {
"telNo":"${telNo}"
}

<#if householdIndicator != 1>
    ,
    "individualCaseId": "${individualCaseId}"
</#if>
}
