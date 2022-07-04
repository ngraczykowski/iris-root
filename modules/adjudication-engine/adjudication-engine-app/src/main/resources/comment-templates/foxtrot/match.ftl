<#import "match-utils.ftl" as matchUtils>

<#-- Templates for standalone features -->
<#import "name-agent.ftl" as nameTemplate>
<#import "date-agent.ftl" as dateTemplate>
<#import "document-agent.ftl" as otherDocumentTemplate>
<#import "national-id-agent.ftl" as nationalIdDocumentTemplate>
<#import "passport-agent.ftl" as passportNumberDocumentTemplate>
<#import "gender-agent.ftl" as genderTemplate>
<#import "nationality-agent.ftl" as nationalityCountryTemplate>
<#import "other-countries.ftl" as countryTemplate>
<#import "bic.ftl" as bicTemplate>

<#-- Templates for grouped features and categories -->
<#import "visa-expiry.ftl" as visaExpiryTemplate>

<#-- If you want to create narrative for group of features/categories, you can define group here -->
<#assign feature_and_category_groups = {
"visaExpiry": {
"features": ["isUaeNationalId", "visaExpiryDateVsToday"],
"categories": [ "hitType", "isHitOnWlName","watchlistType", "recordSourceType"]
}
}>

<#-- If the given feature/group is not listed here, then the comment for this feature will be added
just after features that are included (in unchanged order).
 -->
<#assign priorities = [
'visaExpiry',
'name',
'date',
'nationalIdDocument',
'passportNumberDocument',
'gender',
'nationalityCountry',
'country'
]>

<#-- Will not work for longer feature chains
 -->
<#assign ignoreWhenOtherFeaturesPresent = {
}>

<#function calculatePriority featureName>
    <#local index = priorities?seq_index_of(featureName)>

    <#if index == -1>
        <#return priorities?size>
    </#if>

    <#return index>
</#function>

<#function getSorted objects>
    <#return objects
    ?map(f -> { 'object': f, 'priority': calculatePriority(f.name) })
    ?sort_by('priority')
    ?map(f -> f['object'])>
</#function>


<#function shouldIgnoreDueToOtherFeatures feature featuresList>
    <#if ignoreWhenOtherFeaturesPresent[feature.name]??>
        <#list featuresList as otherFeature>
            <#if ignoreWhenOtherFeaturesPresent[feature.name]?seq_contains(otherFeature.name)>
                <#return true>
            </#if>
        </#list>
    </#if>
    <#return false>
</#function>

<#function hasFeatureOwnTemplate name>
    <#local templateName = "${name}Template">
    <#if .vars[templateName]??>
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>

<#function getTemplate name>
    <#local templateName = "${name}Template">
    <#return .vars[templateName]>
</#function>

<#function generateFeatureComments alertModel matchModel>
    <#local features = []>
    <#local matchFeatures = matchUtils.getMatchFeatures(matchModel) + matchUtils.getMatchGroupedContexts(feature_and_category_groups, matchModel)>
    <#local matchFeatures = matchFeatures
    ?filter(f -> hasFeatureOwnTemplate(f.name))>

    <#local features = features + getSorted(matchFeatures)
    ?filter(f -> getTemplate(f.name)["isSolutionConsistent"](alertModel, matchModel, f))>

    <#local features = features + getSorted(matchFeatures)
    ?filter(f -> !getTemplate(f.name)["isSolutionConsistent"](alertModel, matchModel, f))>

    <#local features = features?filter(f -> getTemplate(f.name)["shouldAdd"](alertModel, matchModel, f))>

    <#return features
    ?filter(f -> !shouldIgnoreDueToOtherFeatures(f, features))
    ?map(f -> getTemplate(f.name)["comment"](alertModel, matchModel, f))>
</#function>

<#macro match alertModel matchModel>
    ${stringUtils.join(generateFeatureComments(alertModel, matchModel), " ")}<#t>
</#macro>
