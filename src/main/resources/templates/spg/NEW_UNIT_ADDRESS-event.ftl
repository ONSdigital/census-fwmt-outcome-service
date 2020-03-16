"newAddress" : {
"sourceCaseId": ${spgOutcome.getSiteCaseId()},
"collectionCase": {
"id": ${generatedUuid},
"caseType": "SPG",
"survey": "CENSUS",
"fieldcoordinatorId": ${spgOutcome.getCoordinatorId()},
"fieldofficerId": ${agentId},
"collectionExerciseId": "32fn45nd-0dbf-4499-bfa7-0aa4mgit8sh54",
"address": {
"addressLine1": ${address.getAddressLine1},
"addressType": "SPG",
"addressLevel": "U"
}
}
}