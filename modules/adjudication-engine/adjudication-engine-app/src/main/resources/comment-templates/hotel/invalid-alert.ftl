<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && !['NO']?seq_contains(featureModel.solution)>
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

    <#if featureModel.solution == 'YES'>
        <#if names?size == 1>
            <#return "Alert was generated against invalid token: " + joinedNames + ".">
        <#elseif (names?size > 1) >
            <#return "Alert was generated against invalid tokens: " + joinedNames + ".">
        </#if>

    <#elseif featureModel.solution == 'FI'>
        <#return "Invalid alert. Alert triggered against financial institution instead of fraudulent counterparty. Alerted Party " + joinedNames + ".">
    </#if>

    <#return "">
</#function>
