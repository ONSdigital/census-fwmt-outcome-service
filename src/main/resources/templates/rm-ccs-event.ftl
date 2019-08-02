{
"event":{
"type":"CCS_ADDRESS_LISTED",
"source":"FIELDWORK_GATEWAY",
"channel":"FIELD",
"dateTime":"${ccsPropertyListingOutcome.eventDate}",
"transactionId":"${ccsPropertyListingOutcome.transactionId}"
},
"payload":{
"CCSProperty":{
"collectionCase":{
"id":"${ccsPropertyListingOutcome.propertyListingCaseId}"
},
"sampleUnit":{
"addressType":"${addressType}",
<#if ccsPropertyListingOutcome.ceDetails??>
    "estabType":"${ccsPropertyListingOutcome.ceDetails.establishmentType}",
<#else>
    <#if addressType == "HH" >
        "estabType":"Household",
    <#elseif addressType == "NR" >
        "estabType":"Non Residential",
    </#if>

</#if>
"addressLevel":"${addressLevel}",
"organisationName":"${organisationName}",
"addressLine1":"${ccsPropertyListingOutcome.address.addressLine1}",
"addressLine2":"${ccsPropertyListingOutcome.address.addressLine2}",
<#if ccsPropertyListingOutcome.address.addressLine3??>
    "addressLine3":"${ccsPropertyListingOutcome.address.addressLine3}",
</#if>
"townName":"${ccsPropertyListingOutcome.address.town}",
"postcode":"${ccsPropertyListingOutcome.address.postCode}",
"latitude":"${ccsPropertyListingOutcome.address.location.latitude?string["0.#######"]}",
"longitude":"${ccsPropertyListingOutcome.address.location.longitude?string["0.#######"]}",
"fieldcoordinatorId":"${ccsPropertyListingOutcome.coordinatorCode}",
"fieldofficerId":"${ccsPropertyListingOutcome.fieldOfficerCode}"
}
${payload}
}
}
}