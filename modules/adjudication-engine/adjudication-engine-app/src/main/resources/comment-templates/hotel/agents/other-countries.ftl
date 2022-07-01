<#import "../match-utils.ftl" as matchUtils>
<#import "country-agent-helper.ftl" as helper>
<#import "nationality-agent.ftl" as nationalityAgent>
<#import "residency-agent.ftl" as residencyAgent>
<#import "incorporation-countries.ftl" as incorporationCountries>
<#import "registration-countries.ftl" as registrationCountries>

<#assign mappings={
"MATCH": "match",
"NO_MATCH": "do not match",
"WEAK_MATCH": "do not match"
}>

<#assign other_country_features = [
"nationalityAgent",
"residencyAgent",
"incorporationCountries",
"registrationCountries"
]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#return helper.isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function hasAnyConclusiveAndConsistentCountryDecision alertModel matchModel>
    <#list matchUtils.getMatchFeatures(matchModel) as feature>
        <#if other_country_features?seq_contains(feature.name)>
            <#if .vars[feature.name]["shouldAdd"](alertModel, matchModel, feature)>
                <#return true>
            </#if>
        </#if>
    </#list>
    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    </#if>
<#--TODO it would be nice to exclude only duplicated values, not the whole comment-->
    <#if hasAnyConclusiveAndConsistentCountryDecision(alertModel, matchModel)>
        <#return false>
    </#if>

    <#return helper.shouldAdd(alertModel, matchModel, featureModel)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#return helper.comment(featureModel, 'other countries', 'countries', mappings)>
</#function>
