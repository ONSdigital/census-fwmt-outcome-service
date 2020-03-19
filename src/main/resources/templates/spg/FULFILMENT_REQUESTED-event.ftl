"fulfilmentRequest" : {
  "fulfilmentCode": "${packcode}",
  "caseId": "${caseId}",
  "address": {
  },
  "contact": {
    <#if title??>
      "title": "${requesterTitle}",
    <#else>
      "title":null,
    </#if>
    <#if forename??>
      "forename":"${requesterForename}",
    <#else>
      "forename":null,
     </#if>
    <#if surname??>
       "surname":"${requesterSurname}",
    <#else>
       "surname":null,
    </#if>
    <#if telNo??>
      "telNo":"${requesterPhone}"
    <#else>
      "telNo":null
    </#if>
  }
  <#if householdIndicator != 1>
    ,
      "individualCaseId": "${individualCaseId}"
  </#if>
}