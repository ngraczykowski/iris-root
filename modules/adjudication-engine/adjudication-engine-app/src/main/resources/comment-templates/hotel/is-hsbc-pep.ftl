
<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && !['INCONCLUSIVE', 'DATA_SOURCE_ERROR']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if ['INCONCLUSIVE', 'NO_DATA', 'DATA_SOURCE_ERROR']?seq_contains(featureModel.solution)>
        <#return false>
    </#if>
    <#return isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#assign sentimentMappings={
'true_pep': 'a True PEP',
'not_pep_anymore': 'no longer a PEP',
'not_pep_any': 'not or no longer a PEP',
'not_pep': 'not a Pep'
}>

<#function comment alertModel matchModel featureModel>
    <#local solution = featureModel.solution>
    <#local reason = featureModel.reason>
    <#local biography = reason.biography>

    <#if solution == 'NOT_PEP_PROCEDURAL'>
        <#if biography?has_content>
            <#return "Watchlist Party is not a PEP according to HSBC policy as he/she has the following biography: ${biography}">
        <#else>
            <#stop "Biography must not be empty for NOT_PEP_ANYMORE">
        </#if>
    <#elseif solution == 'NOT_PEP_ANYMORE'>
        <#if biography?has_content>
            <#return "Watchlist Party is no longer a PEP per HSBC Policy. WP is out of role for more than 5 years. Last held positions: ${biography?trim}">
        <#else>
            <#stop "Biography must not be empty for NOT_PEP_ANYMORE">
        </#if>
    <#elseif solution == 'NOT_PEP_HISTORICAL'>
        <#if biography?has_content>
            <#return "Based on the learning, Watchlist Party is not a PEP according to HSBC Policy as he/she has the following biography: ${biography?trim}">
        <#else>
            <#return "Based on the learning, Watchlist Party is not a PEP.">
        </#if>
    <#else>
        <#stop "Expected 'NOT_PEP_PROCEDURAL', 'NOT_PEP_ANYMORE' or 'NOT_PEP_HISTORICAL' feature solution here, but it was: ${solution?trim}">
    </#if>
</#function>
