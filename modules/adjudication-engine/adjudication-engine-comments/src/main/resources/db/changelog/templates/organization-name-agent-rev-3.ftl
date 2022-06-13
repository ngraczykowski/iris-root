<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="organization name">
<#assign wlAttribute="organization name">

<#assign mappings={
"EXACT_MATCH": "is an exact match with",
"STRONG_MATCH": "matches",
"MATCH": "matches",
"WEAK_MATCH": "does not match",
"NO_MATCH": "does not match",
"HQ_NO_MATCH": "does not match"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['HQ_NO_MATCH', 'NO_MATCH', 'WEAK_MATCH', 'NO_DATA']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['EXACT_MATCH', 'STRONG_MATCH', 'MATCH', 'NO_DATA']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    <#elseif featureModel.solution == "NO_DATA" && matchUtils.getMatchFeatureSolution(matchModel, 'invalidAlert') == 'PREVIOUS_NAMES_ONLY'>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign solution = featureModel.solution>
    <#assign reason = featureModel.reason>

    <#if solution == "NO_DATA">
        <#return "">
    <#else>
        <#assign alertedValues = joinApValues(getApValues(featureModel.reason))>
        <#assign watchlistValues = stringUtils.join(getWpValues(featureModel.reason), ', ')>
        <#return agentUtils.simpleComment(solution, apAttribute, wlAttribute, alertedValues, watchlistValues, mappings)>
    </#if>
</#function>

<#function joinApValues alertedValues>
    <#return stringUtils.join(buildNames(alertedValues), " also known as ")>
</#function>

<#function getApValues reason>
    <#local values = []>
    <#if reason.results??>
        <#list reason.results as result>
            <#if result.alerted_party_name?? && !values?seq_contains(result.alerted_party_name)>
                <#local values = values + [result.alerted_party_name]>
            </#if>
        </#list>
    </#if>
    <#return values>
</#function>

<#function getWpValues reason>
    <#local values = []>
    <#if reason.results??>
        <#list reason.results as result>
            <#if result.watchlist_party_name?? && !values?seq_contains(result.watchlist_party_name)>
                <#local values = values + [result.watchlist_party_name]>
            </#if>
        </#list>
    </#if>
    <#return values>
</#function>

<#function buildNames nameLists...>
    <#assign names = []>
    <#list nameLists as nameList>
        <#list nameList as name>
            <#assign normalized = normalizeName(name)>
            <#if !stringUtils.containsIgnoreCase(names, normalized)>
                <#assign names = names + [normalized]>
            </#if>
        </#list>
    </#list>
    <#return names>
</#function>

<#function normalizeName name>
    <#return name!?replace("\\s+", " ", "rm")?trim>
</#function>
