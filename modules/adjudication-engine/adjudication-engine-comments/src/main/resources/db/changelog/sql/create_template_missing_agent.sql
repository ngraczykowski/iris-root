INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('missing-agent.ftl', '2021-10-26 18:19:33.000000', 1, '<#function isSolutionConsistent alertModel matchModel featureModel>
    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#return true>
</#function>

<#function comment alertModel matchModel featureModel>
    <#return "(Warning! Comment template for agent ${featureModel.name} not available)">
</#function>
');
