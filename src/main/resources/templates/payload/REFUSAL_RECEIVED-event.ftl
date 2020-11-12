"refusal" : {
"type": "${refusalType}",
"agentId": "${officerId}",

<#if (type == "HH") && refusalType == "HARD_REFUSAL">
"isHouseholder": "${isHouseHolder?c}",
"contact": {
"title": "${encryptedTitle}",
"forename": "${encryptedForename}",
"surname": "${encryptedSurname}"
},
</#if>

"collectionCase": {
"id": "${caseId}"
}
}