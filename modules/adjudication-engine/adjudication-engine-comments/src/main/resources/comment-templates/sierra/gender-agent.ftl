<#import "agent-utils.ftl" as agentUtils>

<#assign attribute="gender">
<#assign mappings={
"MATCH": "matches",
"MATCH": "does not completely match",
"WEAK_MATCH": "does not completely match",
"NO_MATCH": "does not match",
"HQ_NO_MATCH": "does not match"
}>
<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "MATCH", "INCONCLUSIVE", "UNKNOWN"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['HQ_NO_MATCH', 'NO_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign solution = featureModel.solution>
    <#assign customerValues = stringUtils.join(featureModel.reason.customerValues, ", ")>
    <#assign watchlistValues = stringUtils.join(featureModel.reason.watchlistValues, ", ")>
<#--    <#if solution == "NO_DATA">-->
<#--        <#return "Alerted Party's ${attribute} could not be resolved.">-->
<#--    </#if>-->
    <#return agentUtils.simpleComment(solution, attribute, attribute, customerValues, watchlistValues, mappings)>
</#function>
