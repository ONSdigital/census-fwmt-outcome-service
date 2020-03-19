"addressTypeChange" : {
  "collectionCase": {
    "id": "${caseId}",
    "ceExpectedResponses”:"${usualResidents}",
    "address" : {
      "addressType":"CE",
      "estabType":"${spgOutcome.ceDetails.establishmentType}",
      <#if householdOutcome.ceDetails.establishmentName??>
        "orgName": "${spgOutcome.ceDetails.establishmentName}"
      <#else>
        "orgName": null
      </#if>
    }
  }
}