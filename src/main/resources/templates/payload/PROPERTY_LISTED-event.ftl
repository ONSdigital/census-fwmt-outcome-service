{
"event": {
  "type": "CCS_ADDRESS_LISTED",
  "source": "FIELDWORK_GATEWAY",
  "channel": "FIELD",
  "dateTime": "${eventDate}",
  "transactionId": "${ccsPropertyListingOutcome.transactionId}"
},
"payload": {
  "CCSProperty": {
    "collectionCase": {
      "id": "${caseId}"
    },
    "interviewRequired": "${interviewRequired}",
    "sampleUnit": {
      "addressType": "${addressType}",
        <#if addressType == CE>
          "estabType": "${outcome.ceDetails.establishmentType}",
          "organisationName": "${outcome.ceDetails.establishmentName}",
        </#if>
      "addressLevel": "${addressLevel}",
      "addressLine1": "${address.addressLine1}",
        <#if address.addressLine2??>
        "addressLine2": "${address.addressLine2}",
        </#if>
        <#if address.addressLine3??>
        "addressLine3": "${address.addressLine3}",
        </#if>
      "townName": "${address.town}",
      "postcode": "${address.postCode}",
      "latitude": "${address.location.latitude?string["0.#######"]}",
      "longitude": "${address.location.longitude?string["0.#######"]}",
      "fieldcoordinatorId": "${outcome.coordinatorCode}",
      "fieldofficerId": "${outcome.fieldOfficerCode}",
      "oa":"${oa}",
      "region": "${region}"
    }
  }
}
}