{
"event" : {
"type" : "${eventType}",
"source" : "FIELDWORK_GATEWAY",
"channel" : "FIELD",
"dateTime" : "${householdOutcome.eventDate}",
"transactionId" : "${householdOutcome.transactionId}"
}
,
"payload" : {
${payload}
}
}
