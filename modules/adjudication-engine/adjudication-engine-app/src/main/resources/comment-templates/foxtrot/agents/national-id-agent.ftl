<#import "agent-utils.ftl" as agentUtils>
<#import "../match-utils.ftl" as matchUtils>

<#assign attribute="national id">

<#assign mappings={
"PERFECT_MATCH": "is an exact match with",
"DIGIT_MATCH": "matches",
"WEAK_DIGIT_MATCH": "is near to",
"WEAK_MATCH": "is near to",
"NO_MATCH": "does not match"
}>
<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "INCONCLUSIVE", "UNKNOWN"]>
<#assign hintedSolutions = ["WEAK_DIGIT_MATCH", "WEAK_MATCH"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['NO_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['PERFECT_MATCH', 'DIGIT_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif isHinted(featureModel.solution) && matchModel.solution?matches('.*NO_DECISION')>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>
    <#return true>
</#function>

<#function isHinted featureSolution>
    <#return hintedSolutions?seq_contains(featureSolution)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign featureSolution = featureModel.solution>
    <#assign customerValues = stringUtils.join(featureModel.reason.customerValues, ", ")>
    <#assign watchlistValues = stringUtils.join(featureModel.reason.watchlistValues, ", ")>
    <#assign output = agentUtils.simpleComment(featureSolution, attribute, attribute, customerValues, watchlistValues, mappings)>
    <#return output>
</#function>
