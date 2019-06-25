{
    "fulfilment" : {
        "productCode": "${productCodeLookup}",
        "caseId" : "${householdOutcome.caseId}",
        <#if householdIndicator != 1>
        "individualCaseId": "${individualCaseId}",
            "address": {},
            "contact": {
                "forename":"${forename}"
                "surname":"${surname}"
                "telNo":"${telNo}"
            }
        </#if>
    }
}