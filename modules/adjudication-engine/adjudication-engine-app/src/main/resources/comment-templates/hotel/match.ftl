<#import "match-utils.ftl" as matchUtils>
<#import "agents/alerted-party-type.ftl" as apTypeTemplate>
<#import "agents/date-agent.ftl" as dateOfBirthTemplate>
<#import "agents/national-id-agent.ftl" as nationalIdDocumentTemplate>
<#import "agents/passport-agent.ftl" as passportNumberDocumentTemplate>
<#import "agents/registration-number-agent.ftl" as registrationNumberDocumentTemplate>
<#import "agents/tax-id-agent.ftl" as taxIdDocumentTemplate>
<#import "agents/document-agent.ftl" as otherDocumentTemplate>
<#import "agents/gender-agent.ftl" as genderTemplate>
<#import "agents/name-agent.ftl" as nameTemplate>
<#import "agents/nationality-agent.ftl" as nationalityCountryTemplate>
<#import "agents/residency-agent.ftl" as residencyCountryTemplate>
<#import "agents/place-of-birth-agent.ftl" as placeOfBirthTemplate>
<#import "agents/wl-type.ftl" as wlTypeTemplate>
<#import "agents/is-hsbc-pep.ftl" as isPepTemplate>
<#import "agents/is-tp-marked.ftl" as isTpMarkedTemplate>
<#import "agents/is-ap-tp-marked.ftl" as isApTpMarkedTemplate>
<#import "agents/is-case-tp-marked.ftl" as isCaseTpMarkedTemplate>
<#import "agents/incorporation-countries.ftl" as incorporationCountryTemplate>
<#import "agents/registration-countries.ftl" as registrationCountryTemplate>
<#import "agents/other-countries.ftl" as otherCountryTemplate>
<#import "agents/invalid-alert.ftl" as invalidAlertTemplate>
<#import "agents/common-names.ftl" as commonNamesTemplate>
<#import "agents/common-ap.ftl" as commonApTemplate>
<#import "agents/common-mp.ftl" as commonMpTemplate>
<#import "agents/logical-discounting-dob.ftl" as logicalDiscountingDobTemplate>
<#import "agents/geo-place-of-birth.ftl" as geoPlaceOfBirthTemplate>
<#import "agents/geo-residency.ftl" as geoResidenciesTemplate>
<#import "agents/news-age.ftl" as newsAgeTemplate>

<#-- If the given feature is not listed here, then the comment for this feature will be added just
after features that are included (in unchanged order).
 -->
<#assign priorities = [
'invalidAlert',
'isPep',
'name',
'dateOfBirth',
'nationalIdDocument',
'passportNumberDocument',
'registrationNumberDocument',
'taxIdDocument',
'otherDocument',
'gender',
'logicalDiscountingDob',
'nationalityCountry',
'residencyCountry',
'placeOfBirth',
'incorporationCountry',
'registrationCountry',
'otherCountry',
'isTpMarked',
'geoPlaceOfBirth',
'geoResidencies',
'commonNames',
'commonAp',
'commonMp',
'isCaseTpMarked',
'isApTpMarked',
'isTpMarked',
'newsAge'
]>

<#-- Will not work for longer feature chains
 -->
<#assign ignoreWhenOtherFeaturesPresent = {
'commonNames': ['invalidAlert'],
'commonAp': ['invalidAlert', 'commonNames'],
'commonMp': ['invalidAlert', 'commonNames', 'commonAp']
}>

<#function calculatePriority featureName>
    <#local index = priorities?seq_index_of(featureName)>

    <#if index == -1>
        <#return priorities?size>
    </#if>

    <#return index>
</#function>

<#function getSortedFeatures features>
    <#return features
    ?map(f -> { 'feature': f, 'priority': calculatePriority(f.name) })
    ?sort_by('priority')
    ?map(f -> f['feature'])>
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

<#function getFeatureTemplate name>
    <#local templateName = "${name}Template">
    <#if .vars[templateName]??>
        <#return .vars[templateName]>
    <#else>
        <#stop "Template not found: ${name}">
    </#if>
</#function>

<#function generateFeatureComments alertModel matchModel>
    <#local features = []>
    <#local matchFeatures = matchUtils.getMatchFeatures(matchModel)>

    <#local features = features + getSortedFeatures(matchFeatures)
    ?filter(f -> getFeatureTemplate(f.name)["isSolutionConsistent"](alertModel, matchModel, f))>

    <#local features = features + getSortedFeatures(matchFeatures)
    ?filter(f -> !getFeatureTemplate(f.name)["isSolutionConsistent"](alertModel, matchModel, f))>

    <#local features = features?filter(f -> getFeatureTemplate(f.name)["shouldAdd"](alertModel, matchModel, f))>

    <#return features
    ?filter(f -> !shouldIgnoreDueToOtherFeatures(f, features))
    ?map(f -> getFeatureTemplate(f.name)["comment"](alertModel, matchModel, f))>
</#function>

<#macro match alertModel matchModel>
    ${stringUtils.join(generateFeatureComments(alertModel, matchModel), " ")}<#t>
</#macro>
