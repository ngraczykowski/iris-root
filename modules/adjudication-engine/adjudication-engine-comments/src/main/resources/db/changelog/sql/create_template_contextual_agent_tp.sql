INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('contextual-agent-tp.ftl', now(), 1, '<#import "match-utils.ftl" as matchUtils>
<#import "agent-utils.ftl" as agentUtils>

<#assign apAttribute="matching text">

<#assign mappings={
"TRUE_POSITIVE": "is a TRUE POSITIVE",
"NOT_TRUE_POSITIVE": "is not a TRUE POSITIVE"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches(''.*TRUE_POSITIVE'') || [''TRUE_POSITIVE'']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches(''.*ERROR'')>
        <#return false>
    </#if>
    <#return isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#assign solution = featureModel.solution>
    <#return "Contextual Learning False Positive agent is a ${solution}.">
</#function>
');
