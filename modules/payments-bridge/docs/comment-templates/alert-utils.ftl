<#import "match.ftl" as matchTemplate/>
<#import "categories.ftl" as categoriesTemplate/>

<#function getMatchDecisions matches>
    <#assign decisions = []>

    <#list matches as match>
        <#assign decisions = decisions + [ match.solution! ] />
    </#list>
    <#return decisions>
</#function>

<#function mapSolutionToText decision>
    <#if decision!?has_content == false>
        <#return "">
    </#if>

    <#if decision=="SOLUTION_POTENTIAL_TRUE_POSITIVE">
        <#return "Potential True Positive">
    </#if>

    <#if decision=="SOLUTION_NO_DECISION">
        <#return "Manual Investigation">
    </#if>

    <#if decision=="SOLUTION_FALSE_POSITIVE">
        <#return "False Positive">
    </#if>

    <#if decision=="SOLUTION_HINTED_FALSE_POSITIVE">
        <#return "Hinted False Positive">
    </#if>

    <#if decision=="SOLUTION_HINTED_POTENTIAL_TRUE_POSITIVE">
        <#return "Hinted Potential True Positive">
    </#if>

    <#return "">
</#function>

<#function mapRecommendationToText decision>
    <#if decision!?has_content == false>
        <#return "">
    </#if>

    <#if decision=="ACTION_POTENTIAL_TRUE_POSITIVE">
        <#return "Potential True Positive">
    </#if>

    <#if decision=="ACTION_INVESTIGATE">
        <#return "Manual Investigation">
    </#if>

    <#if decision=="ACTION_FALSE_POSITIVE">
        <#return "False Positive">
    </#if>

    <#if decision=="ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE">
        <#return "Manual Investigation">
    </#if>

    <#if decision=="ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE">
        <#return "Hinted False Positive">
    </#if>

    <#if decision=="ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE">
        <#return "Hinted Potential True Positive">
    </#if>

    <#return "">
</#function>

<#function getDecisionGroupOutput alertModel decision matches >
    <#assign decisionText = mapSolutionToText(decision!)>
    <#assign output = decisionText+" hits:">
    <#list matches as match>
        <#assign solution = match.solution>
        <#if solution! == decision>
            <#assign output = output + "\n" + "Match "  + match.matchId>
            <#if solution! != "SOLUTION_NO_DECISION">
                <#assign comment>
                    <@matchTemplate.match alertModel match/>
                </#assign>
                <#assign categoryComment>
                    <@categoriesTemplate.match match/>
                </#assign>
                <#assign comment = comment + " "+ categoryComment >
                <#assign output = output + ": " + comment + "\n">
            </#if>
        </#if>
    </#list>

    <#return output?remove_ending("\n")?remove_beginning("\n")>
</#function>

<#function isAnyFurtherActionRequired solution>
    <#if solution!?starts_with("ACTION_INVESTIGATE")>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function addSection current section>
    <#assign output=current>

    <#if section?has_content>
        <#assign output += "\n\n" + section>
    </#if>

    <#return output>
</#function>

<#function generateMatchComments matches alertModel>
    <#assign output="">
    <#assign decisions = getMatchDecisions(matches)>
    <#assign supportedDecisions = [
    "SOLUTION_POTENTIAL_TRUE_POSITIVE",
    "SOLUTION_NO_DECISION",
    "SOLUTION_FALSE_POSITIVE"]>

    <#list supportedDecisions as decision>
        <#if decisions?seq_contains(decision)>
            <#if output?has_content>
                <#assign output+="\n\n">
            </#if>
            <#assign output+=getDecisionGroupOutput(alertModel, decision, matches)>
        </#if>
    </#list>

    <#return output>
</#function>
