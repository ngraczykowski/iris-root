<#import "agent-utils.ftl" as agentUtils>
<#import "match-utils.ftl" as matchUtils>
<#import "national-id-agent.ftl" as nationalIdAgent>

<#assign otherDocumentFeatureNames = ['nationalIdAgent']>
<#assign attribute="passport number">
<#assign mappings={
"PERFECT_MATCH": "is an exact match with",
"DIGIT_MATCH": "matches",
"WEAK_DIGIT_MATCH": "is near to",
"WEAK_MATCH": "is near to",
"NO_MATCH": "does not match"
}>
<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "INCONCLUSIVE", "UNKNOWN"]>
<#assign hintedSolutions = ["WEAK_DIGIT_MATCH", "WEAK_MATCH"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['NO_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['PERFECT_MATCH', 'DIGIT_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#assign matchSolution = matchModel.solution>
    <#assign featureSolution = featureModel.solution>

    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    <#elseif isHinted(featureModel.solution) && matchModel.solution?matches('.*NO_DECISION')>
        <#return false>
    </#if>

    <#assign values = collectAlertedAndMatchedValues(alertModel, matchModel, featureModel)>
    <#if values['alertedValues']?size == 0 || values['matchedValues']?size == 0>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function isHinted featureSolution>
    <#return hintedSolutions?seq_contains(featureSolution)>
</#function>

<#function shouldExcludeFromOtherFeature alertModel matchModel currentFeature otherFeature >
    <#if !otherDocumentFeatureNames?seq_contains(otherFeature.name)>
        <#return false>
    <#elseif !.vars[otherFeature.name]["shouldAdd"](alertModel, matchModel, otherFeature)>
        <#return false>
    </#if>
    <#return true>
</#function>

<#function collectAlertedAndMatchedValues alertModel matchModel featureModel>
    <#assign currentFeature = featureModel>
    <#assign alertedValues = currentFeature.reason.customerValues>
    <#assign matchedValues = currentFeature.reason.watchlistValues>
    <#assign alertedValuesToExclude = []>
    <#assign matchedValuesToExclude = []>

    <#list matchUtils.getMatchFeatures(matchModel) as otherFeature>
        <#if shouldExcludeFromOtherFeature(alertModel, matchModel, currentFeature, otherFeature)>
            <#assign commentFunc = .vars[otherFeature.name]["comment"]>

            <#list alertedValues as alertedValue>
                <#list matchedValues as matchedValue>
                    <#if commentFunc(alertModel, matchModel, otherFeature)?contains(alertedValue) &&
                    commentFunc(alertModel, matchModel, otherFeature)?contains(matchedValue)>
                        <#assign alertedValuesToExclude = alertedValuesToExclude + [alertedValue]>
                        <#assign matchedValuesToExclude = matchedValuesToExclude + [matchedValue]>
                    </#if>
                </#list>
            </#list>
        </#if>
    </#list>

    <#return {
    'alertedValues': alertedValues?filter(v -> !alertedValuesToExclude?seq_contains(v)),
    'matchedValues': matchedValues?filter(v -> !matchedValuesToExclude?seq_contains(v))
    }>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign values = collectAlertedAndMatchedValues(alertModel, matchModel, featureModel)>
    <#assign featureSolution = featureModel.solution>
    <#assign customerValues = stringUtils.join(values['alertedValues'], ", ")>
    <#assign watchlistValues = stringUtils.join(values['matchedValues'], ", ")>
    <#assign output = agentUtils.simpleComment(featureSolution, attribute, attribute, customerValues, watchlistValues, mappings)>
    <#return output>
</#function>
