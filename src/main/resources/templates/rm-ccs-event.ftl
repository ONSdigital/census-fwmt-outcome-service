{
"event" : {
"type" : "${eventType}",
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
"addressType" : "${ccsPropertyListingOutcome}",
"estabType" : "${ccsPropertyListingOutcome}",
"addressLevel" : "${ccsPropertyListingOutcome}",
"organisationName" : "",
"addressLine1" : "${ccsPropertyListingOutcome}",
"addressLine2" : "${ccsPropertyListingOutcome}",
"addressLine3" : "${ccsPropertyListingOutcome}",
"townName" : "${ccsPropertyListingOutcome}",
"postcode" : "${ccsPropertyListingOutcome}",
"latitude" : "${ccsPropertyListingOutcome}",
"longitude" : "${ccsPropertyListingOutcome}",
"fieldcoordinatorId" : "${ccsPropertyListingOutcome}",
"fieldofficerId" : "${ccsPropertyListingOutcome}"
}
,
${payload}
}
}