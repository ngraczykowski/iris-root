<#assign mappings={
'COUNTRY_MATCH': 'matches',
'STATE_MATCH': 'matches',
'CITY_MATCH': 'matches',
'COUNTRY_NO_MATCH': 'does not match',
'STATE_NO_MATCH': 'does not match',
'CITY_NO_MATCH': 'does not match'
}>

<#assign suffixes={
'COUNTRY_MATCH': 'only by country',
'STATE_MATCH': 'only by state'
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['COUNTRY_NO_MATCH', 'STATE_NO_MATCH', 'CITY_NO_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['COUNTRY_MATCH', 'STATE_MATCH', 'CITY_MATCH']?seq_contains(featureModel.solution)>
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

<#function getApValues reason>
    <#local values = []>
    <#list reason.geoCompareResults as result>
        <#if !values?seq_contains(result.alertedPartyValue)>
            <#local values = values + [result.alertedPartyValue]>
        </#if>
    </#list>
    <#return values>
</#function>

<#function getWpValues reason>
    <#local values = []>
    <#list reason.geoCompareResults as result>
        <#if !values?seq_contains(result.watchlistPartyValue)>
            <#local values = values + [result.watchlistPartyValue]>
        </#if>
    </#list>
    <#return values>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign solution = featureModel.solution>
    <#assign alertedValue = stringUtils.join(getApValues(featureModel.reason), ', ')>
    <#assign watchlistValue = stringUtils.join(getWpValues(featureModel.reason), ', ')>
    <#return "Alerted party location/countries (${alertedValue}) ${mappings[solution]} location/country (${watchlistValue}).">
</#function>
