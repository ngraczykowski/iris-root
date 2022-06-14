<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="matching text">

<#assign mappings={
"TRUE_POSITIVE": "True Positive",
"NOT_TRUE_POSITIVE": "not True Positive"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*TRUE_POSITIVE') && ['TRUE_POSITIVE']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    </#if>
    <#return isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign solution = featureModel.solution>
    <#assign alertedPartyId = featureModel.reason.alertedPartyId>
    <#assign watchlistPartyId = featureModel.reason.watchlistPartyId>
    <#assign truePositiveCount = featureModel.reason.true_positive>
    <#return "Alerted context (${alertedPartyId}) and watchlist id (${watchlistPartyId}) was previously evaluated as ${mappings[solution]} (${truePositiveCount}).">
</#function>
