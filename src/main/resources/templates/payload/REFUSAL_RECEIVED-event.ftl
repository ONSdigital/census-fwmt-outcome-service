"refusal" : {
"type": "${refusalType}",
"agentId": "${officerId}",

<#--<#if (type == "HH" || "NC") && refusalType == "HARD_REFUSAL">-->
<#--"isHouseholder": "${refusal.householder}",-->
<#--"contact": {-->
<#--"title": "${encryptedTitle}",-->
<#--"forename": "${encryptedForename}",-->
<#--"surname": "${encryptedSurname}"-->
<#--},-->
<#--</#if>-->

"collectionCase": {
"id": "${caseId}"
}
}