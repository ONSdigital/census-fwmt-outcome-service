"addressTypeChange" : {
  "collectionCase": {
    "id": "${caseId}",
    "ceExpectedCapacity":"${usualResidents}",
    "address" : {
      "addressType":"CE",
      "estabType":"${spgOutcome.ceDetails.establishmentType}",
      <#if spgOutcome.ceDetails.establishmentName??>
        "organisationName": "${spgOutcome.ceDetails.establishmentName}"
      <#else>
        "organisationName": null
      </#if>
    }
  }
}