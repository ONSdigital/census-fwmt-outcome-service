  "CCSProperty": {
    "collectionCase": {
      "id": "${caseId}"
    },
    "interviewRequired": "${interviewRequired}",
    "sampleUnit": {
      "addressType": "${addressType}",
        <#if addressType == "CE">
          "estabType": "${estabType}",
          <#if outcome.ceDetails.establishmentName??>
          "organisationName": "${outcome.ceDetails.establishmentName}",
          <#else>
          "organisationName": "",
          </#if>
        <#else>
          "estabType": "HOUSEHOLD",
        </#if>
      "addressLevel": "${addressLevel}",
      "addressLine1": "${address.addressLine1}",
        <#if address.addressLine2??>
        "addressLine2": "${address.addressLine2}",
        </#if>
        <#if address.addressLine3??>
        "addressLine3": "${address.addressLine3}",
        </#if>
        <#if address.town??>
          "townName": "${address.locality}",
        </#if>
          "postcode": "${address.postcode}",
      "latitude": "${address.latitude?string["0.#######"]}",
      "longitude": "${address.longitude?string["0.#######"]}",
      "fieldCoordinatorId": "${outcome.coordinatorId}",
      "fieldOfficerId": "${outcome.officerId}",
      "oa":"${oa}",
      "region": "${region}"
    }
  }
}
