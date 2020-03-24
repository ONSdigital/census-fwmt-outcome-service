"newAddress":{
  "collectionCase": {
   "id": "${newCaseId}",
    "caseType": "SPG",
    "survey": "CENSUS",
    "fieldcoordinatorId": "${spgOutcome.coordinatorId}",
    "fieldofficerId": "${officerId}",
    "address": {
      "addressLine1": "${address.addressLine1}",
      "addressLine2": "${address.addressLine2}",
      "addressLine3": "${address.addressLine3}",
      "addressType": "SPG",
      "addressLevel": "U",
      "townName": "${address.locality}",
      "postcode": "${address.postCode}",
      "latitude": "${address.latitude}",
      "longitude": "${address.longitude}"
    }
  }
}