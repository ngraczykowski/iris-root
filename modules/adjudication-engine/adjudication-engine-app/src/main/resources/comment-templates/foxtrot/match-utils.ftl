<#function getMatchFeatures matchModel>
    <#return matchModel.features?keys
    ?map(k -> {
    'name': k?remove_beginning("features/"),
    'solution': matchModel.features[k].solution,
    'reason': matchModel.features[k].reason
    })>
</#function>

<#function getMatchCategories matchModel>
    <#return matchModel.categories?keys
    ?map(k -> {
    'name': k?remove_beginning("categories/"),
    'value': matchModel.categories[k]
    })>
</#function>

<#function getMatchGroupedContexts feature_and_category_groups matchModel>
    <#local contexts = []>
    <#list feature_and_category_groups?keys as groupName>
        <#assign featureNames = feature_and_category_groups[groupName]["features"]>
        <#assign categoryNames = feature_and_category_groups[groupName]["categories"]>

        <#local features = getMatchFeatures(matchModel)
        ?filter(f -> featureNames?seq_contains(f.name))>

        <#local categories = getMatchCategories(matchModel)
        ?filter(c -> categoryNames?seq_contains(c.name))>

        <#local contexts = contexts + [{
        "name": groupName,
        "features": features,
        "categories": categories
        }]>
    </#list>
    <#return contexts>
</#function>

<#function getMatchFeatureSolution matchModel featureName>
    <#local possibleFeature = getMatchFeatures(matchModel)?filter(f -> f.name == featureName)>
    <#if possibleFeature?size == 0>
        <#return "">
    <#else>
        <#return possibleFeature[0].solution>
    </#if>
</#function>
