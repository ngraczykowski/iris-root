<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="matching text">

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
    <#assign solution = featureModel.solution>
    <#assign reason = featureModel.reason>

    <#if solution == "NO_DATA">
        <#return "">
    <#else>
        <#local matchedValueGroups = reason.matchedValueGroups>
        <#local alertedValues = [reason.alertedValue!] + reason.alertedValues>
        <#local partyValue = getPartyValue(alertedValues)>
        <#local watchlistValues = getWathlistValues(matchedValueGroups, solution)>
        <#local wlAttribute = getNameType(matchedValueGroups)>
        <#return agentUtils.simpleComment(solution, apAttribute, wlAttribute, partyValue, watchlistValues, mappings)>
    </#if>
</#function>

<#function getPartyValue alertedValues>
    <#return stringUtils.join(buildNames(alertedValues), " also known as ")>
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

<#function getWathlistValues matchedValueGroupsList solution>
    <#if solution == 'NO_MATCH' || solution == 'HQ_NO_MATCH'>
        <#return joinValues(matchedValueGroupsList, 'NO_MATCH', 'HQ_NO_MATCH')>
    <#elseif solution == 'MATCH' || solution = 'WEAK_MATCH'>
        <#return joinValues(matchedValueGroupsList, 'MATCH', 'WEAK_MATCH')>
    <#else>
        <#return joinValues(matchedValueGroupsList, solution)>
    </#if>
</#function>

<#function joinValues matchedValueGroupsList, solutions...>
    <#assign values = []>

    <#list solutions as solution>
        <#list matchedValueGroupsList as matchedValueGroup>
            <#if matchedValueGroup.result == solution>
                <#list matchedValueGroup.watchlistNames as matchedValue>
                    <#assign normalized = normalizeName(matchedValue.name!)>
                    <#if !stringUtils.containsIgnoreCase(values, normalized)>
                        <#assign values = values + [normalized]>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#list>

    <#return stringUtils.join(values, ' also known as ')>
</#function>

<#function getNameType matchedValueGroupsList>
    <#list matchedValueGroupsList as matchedValueGroup>
        <#list matchedValueGroup.watchlistNames as matchedValue>
            <#assign value = matchedValue.type>
        </#list>
    </#list>
    <#return mapNameType(value)>
</#function>

<#function mapNameType nameType>
    <#if nameType == 'NAME' >
        <#return 'name'>
    </#if>
    <#if nameType == 'ALIAS' >
        <#return 'name alias'>
    </#if>
</#function>
