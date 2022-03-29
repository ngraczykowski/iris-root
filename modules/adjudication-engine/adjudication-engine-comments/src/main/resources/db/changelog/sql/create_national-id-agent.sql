INSERT INTO ae_comment_template (template_name, created_at, revision, template) VALUES ('national-id-agent.ftl', '2022-03-29 18:19:26.000000', 1,
'<#import "agent-utils.ftl" as agentUtils>

<#assign document_type_mappings={
"HONG_KONG_NATIONAL_ID": "HKID"
}>

<#assign mappings={
"PERFECT_MATCH": "is an exact match with",
"DIGIT_MATCH": "matches",
"WEAK_DIGIT_MATCH": "is near to",
"WEAK_MATCH": "is near to",
"NO_MATCH": "does not match"
}>
<#assign skippedSolutions = ["AGENT_SKIPPED", "NO_DATA", "INCONCLUSIVE", "UNKNOWN"]>
<#assign hintedSolutions = ["WEAK_DIGIT_MATCH", "WEAK_MATCH"]>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches(''.*FALSE_POSITIVE'') && [''NO_MATCH'', ''WEAK_DIGIT_MATCH'', ''WEAK_MATCH'']?seq_contains(featureModel.solution)>
        <#return true>
    <#elseif matchModel.solution?matches(''.*TRUE_POSITIVE'') && [''PERFECT_MATCH'', ''DIGIT_MATCH'', ''WEAK_DIGIT_MATCH'', ''WEAK_MATCH'']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches(''.*ERROR'')>
        <#return false>
    <#elseif skippedSolutions?seq_contains(featureModel.solution)>
        <#return false>
    <#elseif isHinted(featureModel.solution) && matchModel.solution?matches(''.*NO_DECISION'')>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, featureModel)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function isHinted featureSolution>
    <#return hintedSolutions?seq_contains(featureSolution)>
</#function>

<#function comment alertModel matchModel featureModel>
    <#return createCommentFromReason(featureModel.reason)>
</#function>

<#function collectDocTypes reason>
    <#assign docTypes = []>

    <#list reason.results as result>
        <#if result.solution == reason.solution>
            <#list result.recognizedTypes as docType>
                <#if !docTypes?seq_contains(docType)>
                    <#assign docTypes = docTypes + [docType]>
                </#if>
            </#list>
        </#if>
    </#list>

    <#return docTypes>
</#function>

<#function createCommentFromReason reason>
    <#assign comments = []>
    <#local reasonSolution = reason.solution>

    <#list collectDocTypes(reason) as docType>
        <#assign alertedValues = []>
        <#assign matchedValues = []>

        <#list reason.results as result>
            <#if result.solution == reasonSolution>
                <#if result.recognizedTypes?seq_contains(docType)>
                    <#local alertedDocumentNumber=result.alertedDocumentNumber>
                    <#if !alertedValues?seq_contains(alertedDocumentNumber)>
                        <#assign alertedValues = alertedValues + [alertedDocumentNumber]>
                    </#if>
                    <#local matchedDocumentNumber=result.matchedDocumentNumber>
                    <#if !matchedValues?seq_contains(matchedDocumentNumber)>
                        <#assign matchedValues = matchedValues + [matchedDocumentNumber]>
                    </#if>
                </#if>
            </#if>
        </#list>

        <#if alertedValues?has_content && matchedValues?has_content>
            <#assign attribute = document_type_mappings[docType]!"National ID">
            <#assign partyValue = stringUtils.join(alertedValues, '', '')>
            <#assign watchlistValues = stringUtils.join(matchedValues, '', '')>
            <#assign output = agentUtils.simpleComment(reasonSolution, attribute, attribute, partyValue, watchlistValues, mappings)>
            <#assign comments = comments + [output]>
        </#if>
    </#list>

    <#return stringUtils.join(comments, " ")>
</#function>


');
