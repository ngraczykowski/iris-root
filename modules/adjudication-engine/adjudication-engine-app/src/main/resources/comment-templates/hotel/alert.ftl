<#import "alert-utils.ftl" as alertUtils/>

<#function generateComment alertModel>
    <#local alertDecisionText = alertUtils.mapRecommendationToText(alertModel.recommendedAction!)>
    <#local output="S8 recommended action: " + alertDecisionText>

    <#local newMatchesComments = alertUtils.generateMatchComments(alertModel.matches, alertModel)>
    <#local output=alertUtils.addSection(output, newMatchesComments)>

    <#return output>
</#function>

<#macro alert alertModel>
    ${generateComment(alertModel)?truncate(4000)}<#t>
</#macro>

<@alert .data_model/>
