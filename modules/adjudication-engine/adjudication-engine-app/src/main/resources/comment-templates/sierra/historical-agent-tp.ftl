<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="matching text">

<#assign mappings={
"TRUE_POSITIVE": "is a TRUE POSITIVE",
"NOT_TRUE_POSITIVE": "is not a TRUE POSITIVE"
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
    <#return "Alerted paaaarty account (${alertedPartyId}) and watchlist id (${watchlistPartyId}) was previously evaluated as Potential True Positive (${truePositiveCount}).">
</#function>
