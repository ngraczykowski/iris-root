<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="matching text">

<#assign mappings={
"FALSE_POSITIVE": "False Positive",
"NOT_FALSE_POSITIVE": "not False Positive"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['FALSE_POSITIVE']?seq_contains(featureModel.solution)>
        <#if ['FALSE_POSITIVE']?seq_contains(featureModel.solution)>
            <#return true>
        </#if>
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
    <#assign falsePositiveCount = featureModel.reason.false_positive>
    <#return "Alerted context (${alertedPartyId}) and watchlist id (${watchlistPartyId}) was previously evaluated as ${mappings[solution]} (${falsePositiveCount}).">
</#function>
