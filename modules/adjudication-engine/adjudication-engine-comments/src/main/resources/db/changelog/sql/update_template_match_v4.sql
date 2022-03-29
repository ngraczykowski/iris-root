INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('match.ftl', now(), 4, '<#import "match-utils.ftl" as matchUtils>
<#import "match-utils.ftl" as matchUtils>
<#import "name-agent.ftl" as nameTemplate>
<#import "organization-name-agent.ftl" as organizationNameTemplate>
<#import "organization-name-agent.ftl" as organizationName2Template>
<#import "geo-agent.ftl" as geoTemplate>
<#import "geo-agent.ftl" as geo2Template>
<#import "missing-agent.ftl" as missingAgentTemplate>
<#import "name-matched-text-agent.ftl" as nameMatchedTextTemplate>
<#import "historical-agent-tp.ftl" as historicalRiskAccountNumberTPTemplate>
<#import "historical-agent-fp.ftl" as historicalRiskAccountNumberFPTemplate>
<#import "nationality-agent.ftl" as nationalityCountryTemplate>
<#import "residency-agent.ftl" as residencyCountryTemplate>
<#import "gender-agent.ftl" as genderTemplate>
<#import "national-id-agent.ftl" as nationalIdDocumentTemplate>
<#import "date-agent.ftl" as dateTemplate>
<#import "passport-agent.ftl" as passportTemplate>
<#import "document-agent.ftl" as documentTemplate>

<#-- If the given feature is not listed here, then the comment for this feature will be added just
after features that are included (in unchanged order).
 -->
<#assign priorities = [
''name'',
''organizationName'',
''geo''
]>

<#-- Will not work for longer feature chains
 -->
<#assign ignoreWhenOtherFeaturesPresent = {
''commonNames'': [''invalidAlert''],
''commonAp'': [''invalidAlert'', ''commonNames''],
''commonMp'': [''invalidAlert'', ''commonNames'', ''commonAp'']
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
    ?map(f -> { ''feature'': f, ''priority'': calculatePriority(f.name) })
    ?sort_by(''priority'')
    ?map(f -> f[''feature''])>
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
        <#return .vars["missingAgentTemplate"]>
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

')
