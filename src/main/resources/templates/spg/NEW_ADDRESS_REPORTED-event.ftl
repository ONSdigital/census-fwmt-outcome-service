"newAddress":{
  "sourceCaseId": ${spgOutcome.siteCaseId},
  "collectionCase": {
   "id": ${newCaseId},
    "caseType": "SPG",
    "survey": "CENSUS",
    "fieldcoordinatorId": ${spgOutcome.coordinatorId},
    "fieldofficerId": ${officerId},
    "address": {
      "addressLine1": ${address.getAddressLine1},
      "addressLine2": ${address.getAddressLine2},
      "addressLine3": ${address.getAddressLine3},
      "addressType": "SPG",
      "addressLevel": "U",
      "townName": ${address.getLocality},
      "postcode": ${address.getPostcode},
      "latitude": ${address.getLatitude},
      "longitude": ${address.getLongitude}
    }
  }
}