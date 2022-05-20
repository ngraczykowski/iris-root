INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('country-agent-helper.ftl', '2022-03-29 18:19:26.000000', 1,
'<#import "agent-utils.ftl" as agentUtils>

<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "INCONCLUSIVE", "UNKNOWN"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#assign matchSolution = matchModel.solution>
    <#assign featureSolution = featureModel.solution>
    <#-- Not showing WEAK_MATCH commentary as we HK vs CN is for HSBC the same place -->
    <#if matchSolution?matches(''.*FALSE_POSITIVE'') && ["NO_MATCH"]?seq_contains(featureSolution)>
        <#return true>
    <#elseif matchSolution?matches(''.*TRUE_POSITIVE'') && ["MATCH"]?seq_contains(featureSolution)>
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches(''.*ERROR'')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function getCustomerValue solution reason>
    <#if solution == ''WEAK_MATCH'' || solution == ''NO_MATCH''>
        <#return stringUtils.join(reason.customerValues, '', '')>
    <#else>
        <#return reason.customerValue!>
    </#if>
</#function>

<#function getWatchlistValue solution reason>
    <#if solution == ''WEAK_MATCH'' || solution == ''NO_MATCH''>
        <#return stringUtils.join(reason.watchlistValues, '', '')>
    <#else>
        <#return stringUtils.join(reason.matchedValues, '', '')>
    </#if>
</#function>

<#function comment featureModel apCountryType  mpCountryType mappings>
    <#assign solution = featureModel.solution>
    <#assign reason = featureModel.reason>
    <#assign customerValue = getCustomerValue(solution, reason)>
    <#assign watchlistValues = getWatchlistValue(solution, reason)>
<#--    <#if featureSolution == "NO_DATA">-->
<#--        <#return "Alerted Party''s ${attribute} could not be resolved">-->
<#--    </#if>-->
    <#return agentUtils.simpleComment(solution, apCountryType, mpCountryType, customerValue, watchlistValues, mappings)>
</#function>
');
