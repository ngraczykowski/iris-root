<#function getMatchFeatures matchModel>
    <#local solvingFeatures = matchModel.reason.features>

    <#return matchModel.features?keys
    ?filter(k -> solvingFeatures?seq_contains(k))
    ?map(k -> {
    'name': k?remove_beginning("features/"),
    'solution': matchModel.features[k].solution,
    'reason': matchModel.features[k].reason
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
