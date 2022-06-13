<#function shouldAddCrossmatch matchModel>
    <#local crossmatchCategory = 'categories/crossmatch'>

    <#if matchModel.categories?has_content && matchModel.reason.categories?seq_contains(crossmatchCategory)>
        <#if matchModel.categories[crossmatchCategory]?? &&  matchModel.categories[crossmatchCategory] == 'CROSSMATCH'>
            <#return true>
        </#if>
    </#if>
    <#return false>
</#function>

<#function shouldAddcompanyNameSurrounding matchModel>
    <#local companyNameSurroundingCategory = 'categories/companyNameSurrounding'>

    <#if matchModel.categories?has_content && matchModel.reason.categories?seq_contains(companyNameSurroundingCategory)>
        <#if matchModel.categories[companyNameSurroundingCategory]??  && ['MATCH_1', 'MATCH_2', 'MATCH_3', 'MATCH_4', 'MATCH_5_OR_MORE']?seq_contains(matchModel.categories[companyNameSurroundingCategory])>
            <#return true>
        </#if>
    </#if>
    <#return false>
</#function>

<#function comment matchModel>
    <#assign categoriesComment = "">

    <#if shouldAddCrossmatch(matchModel)>
        <#assign categoriesComment = "Alerted party's address is a " + matchModel.categories['categories/crossmatch']?lower_case +  " to watchlist party name.">
    </#if>
    <#if shouldAddcompanyNameSurrounding(matchModel)>
        <#assign categoriesComment = categoriesComment + "Alerted Party Organization is a crossmatch to Watchlist Party">
        <#if matchModel.categories['categories/watchlistType']??>
            <#assign categoriesComment = categoriesComment + " " + matchModel.categories['categories/watchlistType']?lower_case?cap_first>
        </#if>
        <#assign categoriesComment = categoriesComment + ".">
    </#if>
    <#return categoriesComment>
</#function>

<#macro match matchModel>
    ${comment(matchModel)}<#t>
</#macro>
