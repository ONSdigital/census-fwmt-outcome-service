{
"event" : {
"type" : "CCS_ADDRESS_LISTED",
"source" : "FIELDWORK_GATEWAY",
"channel" : "FIELD",
"transactionId" : "${ccsPropertyListingOutcome.transactionId}",
"dateTime" : "${ccsPropertyListingOutcome.eventDate}"
}
,
"payload" : {
"CCSProperty" : {
"collectionCase" : {
"id":"${ccsPropertyListingOutcome}"
}
,
"sampleUnit" : {
"addressType" : "${addressType}",
"estabType" : "${ccsPropertyListingOutcome.ceDetails.establishmentType}",
"addressLevel" : "${addressLevel}",
"organisationName" : ${organisationName},
"addressLine1" : "${ccsPropertyListingOutcome.address.addressLine1}",
"addressLine2" : "${ccsPropertyListingOutcome.address.addressLine2}",
"addressLine3" : "${ccsPropertyListingOutcome.address.addressLine3}",
"townName" : "${ccsPropertyListingOutcome.address.town}",
"postcode" : "${ccsPropertyListingOutcome.address.postCode}",
"latitude" : "${ccsPropertyListingOutcome.address.location.latitude}",
"longitude" : "${ccsPropertyListingOutcome.address.location.longitude}",
"fieldcoordinatorId" : "${ccsPropertyListingOutcome.address.coordinatiorCode}",
"fieldofficerId" : "${ccsPropertyListingOutcome.address.fieldOfficerCode}"
}
${payload}
}
}