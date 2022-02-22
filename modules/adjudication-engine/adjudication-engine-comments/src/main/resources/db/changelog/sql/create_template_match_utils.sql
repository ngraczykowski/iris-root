INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('match-utils.ftl', '2021-10-26 18:19:31.000000', 1, '<#function getMatchFeatures matchModel>
    <#return matchModel.features?keys
    ?map(k -> {
    ''name'': k?remove_beginning("features/"),
    ''solution'': matchModel.features[k].solution,
    ''reason'': matchModel.features[k].reason
    })>
</#function>

<#function getMatchFeatureSolution matchModel featureName>
    <#local possibleFeature = getMatchFeatures(matchModel)?filter(f -> f.name == featureName)>
    <#if possibleFeature?size == 0>
        <#return "">
    <#else>
        <#return possibleFeature[0].solution>
    </#if>
</#function>
');
