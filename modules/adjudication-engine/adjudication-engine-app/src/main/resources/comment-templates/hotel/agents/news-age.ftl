<#function shouldAdd alertModel matchModel featureModel>
    <#if ['INCONCLUSIVE', 'NO_DATA', 'DATA_SOURCE_ERROR']?seq_contains(featureModel.solution)>
        <#return false>
    </#if>
    <#return isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && !['INCONCLUSIVE', 'DATA_SOURCE_ERROR']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function getMatchCategories matchModel>
    <#return matchModel.categories?keys
    ?map(k -> {
    'name': k?remove_beginning("categories/"),
    'value': matchModel.categories[k]
    })>
</#function>


<#function getHitType categories>
    <#local filteredCategories = categories?filter(c -> c.name == 'hitType')>
    <#if filteredCategories?size == 1>
        <#return filteredCategories[0].value>
    <#else>
        <#return "">
    </#if>
</#function>

<#function getTerrorTelated categories>
    <#local filteredCategories = categories?filter(c -> c.name == 'terrorRelated')>
    <#if filteredCategories?size == 1>
        <#return filteredCategories[0].value>
    <#else>
        <#return "">
    </#if>
</#function>

<#function comment alertModel matchModel featureModel>

    <#local categories = getMatchCategories(matchModel)>
    <#local hitType = getHitType( categories )>
    <#local terrorTelated = getTerrorTelated(categories)>

    <#if hitType == "NNS">

        <#local solution = featureModel.solution>
        <#local reason = featureModel.reason>
        <#if reason?has_content>
            <#if terrorTelated == "YES">
                <#return "Negative news is related to Terror. Negative News is from ${reason.extracted_event_date}: ${reason.reports_text}">
            <#else>
                <#return "Negative News is from ${reason.extracted_event_date}: ${reason.reports_text}">
            </#if>
        <#else>
            <#return "">
        </#if>
    <#else>
        <#return "">
    </#if>

</#function>
