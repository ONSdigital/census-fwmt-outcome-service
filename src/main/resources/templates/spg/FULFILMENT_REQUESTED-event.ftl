"fulfilmentRequest" : {
  "fulfilmentCode": "${packcode}",
  "caseId": "${caseId}",
  "address": {
  },
  "contact": {
    <#if requesterTitle??>
      "title": "${requesterTitle}",
    <#else>
      "title":null,
    </#if>
    <#if requesterForename??>
      "forename":"${requesterForename}",
    <#else>
      "forename":null,
     </#if>
    <#if requesterSurname??>
       "surname":"${requesterSurname}",
    <#else>
       "surname":null,
    </#if>
    <#if requesterPhone??>
      "telNo":"${requesterPhone}"
    <#else>
      "telNo":null
    </#if>
  }
  <#if householdIndicator == 1>
    ,
      "individualCaseId": "${individualCaseId}"
  </#if>
}