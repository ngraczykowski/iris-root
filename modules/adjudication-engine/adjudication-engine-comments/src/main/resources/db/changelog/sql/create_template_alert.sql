
INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('alert.ftl', '2021-10-26 18:17:10.000000', 1, '<#import "alert-utils.ftl" as alertUtils/>

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

');
