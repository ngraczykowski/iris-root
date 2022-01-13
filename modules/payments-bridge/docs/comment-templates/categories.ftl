<#function shouldAdd matchModel>
    <#if matchModel.categories?has_content>
        <#if matchModel.categories['categories/crossmatch']?? &&  matchModel.categories['categories/crossmatch'] == 'CROSSMATCH'>
            <#return true>
        </#if>
    </#if>
    <#return false>
</#function>

<#function comment matchModel>
    <#if shouldAdd(matchModel)>
        <#return "Alerted party's address is a " + matchModel.categories['categories/crossmatch']?lower_case +  " to watchlist party name.">
    </#if>
    <#return "">
</#function>

<#macro match matchModel>
    ${comment(matchModel)}<#t>
</#macro>
