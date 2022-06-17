<#function isSolutionConsistent alertModel matchModel featureModel>
    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#return "(Warning! Comment template for agent ${featureModel.name} not available)">
</#function>
