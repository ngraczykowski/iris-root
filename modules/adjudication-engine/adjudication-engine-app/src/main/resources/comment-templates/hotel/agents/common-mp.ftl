<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && ['YES']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    </#if>
    <#if !matchModel.reason.step_title?matches(".*Common.*")>
        <#return false>
    </#if>
    <#if !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#local names = []>
    <#local reason = featureModel.reason>

    <#if featureModel.reason.names??>
         <#assign names = reason.names>
    <#else>
        <#local partials = (reason.matchedCharacteristics)![]>

        <#list partials as partial>
            <#local solution = partial.solution>
            <#if solution == "YES">
                <#local characteristicValue = partial.characteristicValue>
                <#local names = names + ['${characteristicValue}']>
            <#else>
                <#return solution>
            </#if>

        </#list>
    </#if>

    <#assign joinedNames = stringUtils.join(names, ', ')>

    <#assign name = chooseShortestName(names)>
    <#return "Alert triggered against a common Watchlist Party - ${name}. No additional information available. Hence recommended a risk based closure.">
</#function>


<#function chooseShortestName names>
    <#assign chosenName = names[0]!"">
    <#list names as name>
        <#if name?length < chosenName?length>
            <#assign chosenName = name>
        </#if>
    </#list>
    <#return chosenName>
</#function>
