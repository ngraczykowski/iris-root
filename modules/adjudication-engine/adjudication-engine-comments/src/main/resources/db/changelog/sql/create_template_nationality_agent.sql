INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('nationality-agent.ftl', '2022-03-29 18:19:26.000000', 1,
'<#import "country-agent-helper.ftl" as helper>

<#assign mappings={
"MATCH": "matches",
"NO_MATCH": "does not match",
"WEAK_MATCH": "does not match"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#return helper.isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#return helper.shouldAdd(alertModel, matchModel, featureModel)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#return helper.comment(featureModel, ''nationality country'', ''country'', mappings)>
</#function>
');
