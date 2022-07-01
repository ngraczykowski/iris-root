<#import "country-agent-helper.ftl" as helper>

<#assign mappings={
"MATCH": "matches",
"NO_MATCH": "does not match",
"WEAK_MATCH": "does not match"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#return helper.isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#return helper.shouldAdd(alertModel, matchModel, featureModel)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#local wlType = alertModel.commentInput.wlType>
    <#if wlType! != "SSC">
        <#return helper.comment(featureModel, 'place of birth country', 'country', mappings)>
    <#else>
        <#return "">
    </#if>
</#function>
