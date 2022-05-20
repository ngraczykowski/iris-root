<#function shouldAdd matchModel>
    <#local crossmatchCategory = 'categories/crossmatch'>

    <#if matchModel.categories?has_content && matchModel.reason.categories?seq_contains(crossmatchCategory)>
        <#if matchModel.categories[crossmatchCategory]?? &&  matchModel.categories[crossmatchCategory] == 'CROSSMATCH'>
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
