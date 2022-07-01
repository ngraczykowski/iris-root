<#import "agent-utils.ftl" as agentUtils>

<#assign mappings={
    "PERFECT_MATCH": "is an exact match with",
    "DIGIT_MATCH": "matches",
    "WEAK_DIGIT_MATCH": "is near to",
    "WEAK_MATCH": "is near to",
    "NO_MATCH": "does not match"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['NO_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['PERFECT_MATCH', 'DIGIT_MATCH', 'WEAK_DIGIT_MATCH', 'WEAK_MATCH']?seq_contains(featureModel.solution)>
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


<#function comment alertModel matchModel featureModel>
    <#local featureSolution = featureModel.solution>
    <#local attribute = "registration number">

    <#local customerValues = stringUtils.join(featureModel.reason.customerValues, ", ")>
    <#local watchlistValues = stringUtils.join(featureModel.reason.watchlistValues, ", ")>

    <#return agentUtils.simpleComment(featureSolution, attribute, attribute, customerValues, watchlistValues, mappings)>
</#function>
