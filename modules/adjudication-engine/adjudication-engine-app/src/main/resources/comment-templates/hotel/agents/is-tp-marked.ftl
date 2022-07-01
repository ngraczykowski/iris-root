<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*TRUE_POSITIVE')>
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
    <#return "Watchlist Party's related alerts have historically been escalated as True Matches.">
</#function>
