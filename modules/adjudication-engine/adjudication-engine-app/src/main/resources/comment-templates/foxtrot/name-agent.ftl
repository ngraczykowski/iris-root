<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="name">

<#assign mappings={
"EXACT_MATCH": "is an exact match with",
"STRONG_MATCH": "matches",
"MATCH": "matches",
"WEAK_MATCH": "does not match",
"NO_MATCH": "does not match",
"HQ_NO_MATCH": "does not match"
}>

<#assign wlNameLimit = 3>

<#assign wlNameTypePriorities = [
"REGULAR",
"NAME",
"ALIAS"
]>

<#assign wlNameTypeMappings = {
"REGULAR": "name",
"NAME": "name",
"ALIAS": "name alias"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['HQ_NO_MATCH', 'NO_MATCH', 'WEAK_MATCH', 'NO_DATA']?seq_contains(featureModel.solution)>
        <#if !matchModel.reason.step_title?matches(".*name.*", 'i')>
            <#return false>
        </#if>
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
    <#local solution = featureModel.solution>
    <#local reason = featureModel.reason>

    <#if solution == "NO_DATA">
        <#return "">
    </#if>

    <#local alertedNames = getAlertedPartyNames(reason)>
    <#local watchlistNames = getWatchlistPartyNames(reason, solution)>
    <#local partyValue = formatPartyValue(alertedNames)>
    <#local watchlistValue = formatWatchlistValue(watchlistNames)>
    <#local wlAttribute = formatWlAttribute(watchlistNames)>

    <#return agentUtils.simpleComment(solution, apAttribute, wlAttribute, partyValue, watchlistValue, mappings)>
</#function>

<#function formatPartyValue alertedValues>
    <#return stringUtils.join(buildNames(alertedValues), " also known as ")>
</#function>

<#function getAlertedPartyNames reason>
    <#if reason.chineseScriptSolution?has_content
    && reason.chineseScriptSolution.alertedPartyNames!?size gt 0>
        <#return reason.chineseScriptSolution.alertedPartyNames>
    </#if>

    <#return [reason.alertedValue!] + reason.alertedValues>
</#function>

<#function getWatchlistPartyNames reason solution>
    <#if reason.chineseScriptSolution?has_content
    && reason.chineseScriptSolution.watchlistNames!?size gt 0>
        <#return reason.chineseScriptSolution.watchlistNames
        ?map(name -> { 'value': name, 'type': 'NAME' })>
    </#if>

    <#return getWatchlistNamesForSolution(reason.matchedValueGroups, solution)>
</#function>

<#function buildNames nameLists...>
    <#local names = []>
    <#list nameLists as nameList>
        <#list nameList as name>
            <#local normalized = normalizeName(name)>
            <#if !stringUtils.containsIgnoreCase(names, normalized)>
                <#local names = names + [normalized]>
            </#if>
        </#list>
    </#list>
    <#return names>
</#function>

<#function normalizeName name>
    <#return name!?replace("\\s+", " ", "rm")?trim>
</#function>

<#function getWatchlistNamesForSolution matchedValueGroupsList solution>
    <#if solution == 'NO_MATCH' || solution == 'HQ_NO_MATCH'>
        <#return getWatchlistNamesFilteredBySolutions(matchedValueGroupsList, 'NO_MATCH', 'HQ_NO_MATCH')>
    <#elseif solution == 'MATCH' || solution = 'WEAK_MATCH'>
        <#return getWatchlistNamesFilteredBySolutions(matchedValueGroupsList, 'MATCH', 'WEAK_MATCH')>
    <#else>
        <#return getWatchlistNamesFilteredBySolutions(matchedValueGroupsList, solution)>
    </#if>
</#function>

<#function getWatchlistNamesFilteredBySolutions matchedValueGroupsList, solutions...>
    <#local values = []>

    <#list solutions as solution>
        <#list matchedValueGroupsList as matchedValueGroup>
            <#if matchedValueGroup.result == solution>
                <#list matchedValueGroup.watchlistNames as matchedValue>
                    <#local values = values + [{
                    'name': {
                    'value': matchedValue.name,
                    'type': matchedValue.type
                    },
                    'priority': wlNameTypePriorities?seq_index_of(matchedValue.type)
                    }]>
                </#list>
            </#if>
        </#list>
    </#list>

    <#return values?sort_by('priority')?map(v -> v['name'])>
</#function>

<#function formatWatchlistValue watchlistNames>
    <#local values = []>
    <#list watchlistNames as watchlistName>
        <#local normalized = normalizeName(watchlistName['value']!)>
        <#if !stringUtils.containsIgnoreCase(values, normalized)>
            <#local values = values + [normalized]>
        </#if>
    </#list>>

    <#local limitedValues = values[0..*wlNameLimit]>
    <#local limitedValuesFormatted = stringUtils.join(limitedValues, " also known as ")>

    <#if values?size gt wlNameLimit>
        <#local otherAliasesCount = values?size - wlNameLimit>
        <#return "${limitedValuesFormatted} also known under ${otherAliasesCount} other alias(es) which have also been considered">
    </#if>

    <#return limitedValuesFormatted>
</#function>


<#function formatWlAttribute watchlistNames>
    <#if watchlistNames?size == 0>
        <#stop "Watchlist names should never be empty here">
    </#if>

    <#local firstWatchlistNameType = watchlistNames[0]['type']?upper_case>

    <#if wlNameTypeMappings[firstWatchlistNameType]??>
        <#return wlNameTypeMappings[firstWatchlistNameType]>
    </#if>

    <#stop "Unknown name type: ${firstWatchlistNameType}">
</#function>
