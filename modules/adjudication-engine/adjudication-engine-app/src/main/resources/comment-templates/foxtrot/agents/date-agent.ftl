<#import "agent-utils.ftl" as utils>
<#import "../match-utils.ftl" as matchUtils>

<#assign falsePositiveMappings={
"NEAR": "does not match",
"OUT_OF_RANGE": "does not match"
}>

<#assign truePositiveMappings={
"EXACT": "matches",
"NEAR": "is a potential match to",
"YEAR_2Y": "is a potential match to",
"OUT_OF_RANGE": "does not match"
}>

<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "YEAR_2Y", "INCONCLUSIVE", "UNKNOWN"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['OUT_OF_RANGE', 'NEAR']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches('.*TRUE_POSITIVE') && ['EXACT', 'NEAR', 'YEAR_2Y']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign featureSolution = featureModel.solution>

    <#if featureSolution == "OUT_OF_RANGE_LOGICAL_DISCOUNTING">
        <#assign customerMatchDate = featureModel.reason.customerMatchDate>
        <#assign watchlistMatchDate = featureModel.reason.watchlistMatchDate>
        <#assign minAge = featureModel.reason.config.minAge>
        <#return "Watchlist party event date (${watchlistMatchDate}) occurred <= ${minAge} years after Alerted Party DoB (${customerMatchDate}).">
    </#if>

    <#assign customerDateTypes = getCustomerDateTypes(getApType(matchModel))>
    <#assign customerValues = stringUtils.join(featureModel.reason.customerValues, ", ")>
    <#assign watchlistValues = stringUtils.join(featureModel.reason.watchlistValues, ", ")>
<#--    <#if featureSolution == "NO_DATA">-->
<#--        <#return "Alerted Party's ${attribute} could not be resolved">-->
<#--    </#if>-->
    <#assign mappings = getMappings(matchModel.solution)>
    <#return utils.simpleComment(featureSolution, customerDateTypes, customerDateTypes, customerValues, watchlistValues, mappings)>
</#function>

<#function getApType(matchModel)>
    <#local features = matchUtils.getMatchCategories(matchModel)>
    <#return features
    ?filter(f -> f["name"] == "customerType")
    ?map(f -> f["value"])[0]!"">
</#function>

<#function getMappings matchSolution>
    <#if matchSolution?matches('.*FALSE_POSITIVE')>
        <#return falsePositiveMappings>
    <#else>
        <#return truePositiveMappings>
    </#if>
</#function>

<#function getCustomerDateTypes apType>
    <#if apType == "I">
        <#return "DOB/YOB">
    <#elseif apType == "C">
        <#return "DOI/YOI">
    <#else>
        <#return "DOB/YOB/DOI/YOI">
    </#if>
</#function>
