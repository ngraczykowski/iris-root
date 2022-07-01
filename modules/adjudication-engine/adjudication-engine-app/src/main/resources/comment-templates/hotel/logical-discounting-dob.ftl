<#assign reason_keys_mappings={
"ap_probably_too_young": "is probably too young",
"ap_too_young": "is too young",
"ap_probably_too_old": "is probably too old",
"ap_too_old": "is too old"
}>

<#function isSolutionConsistent alertModel matchModel featureModel>
    <#if matchModel.solution?matches('.*FALSE_POSITIVE') && !['INCONCLUSIVE']?seq_contains(featureModel.solution)>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function shouldAdd alertModel matchModel featureModel>
    <#if featureModel.solution?matches('.*ERROR')>
        <#return false>
    </#if>
    <#return isSolutionConsistent(alertModel, matchModel, featureModel)>
</#function>

<#function extractEstimatedYear ruleReason>
    <#local reducedReasonKey = ruleReason.reduced_reason_key!"">

    <#local lower = []>
    <#local upper = []>

    <#list ruleReason.partials as partial>
        <#local reasonKey = partial.reason_key!"">
        <#if reducedReasonKey == reasonKey>
             <#local reason = partial.reason>
             <#local estimated = reason.estimated_yob>
             <#if estimated?has_content>
                <#if estimated?first?has_content>
                    <#local lower = lower + [estimated?first]>
                </#if>
                <#if estimated?last?has_content>
                    <#local upper = upper + [estimated?last]>
                </#if>
             </#if>
        </#if>
    </#list>

    <#return {
        'lower': lower?max!,
        'upper': upper?min!
    }>
</#function>

<#function extractPartyValues ruleReason>
    <#local reducedReasonKey = ruleReason.reduced_reason_key!"">

    <#local apValues = []>
    <#local mpValues = []>

    <#list ruleReason.partials as partial>
        <#local reasonKey = partial.reason_key!"">
        <#if reducedReasonKey == reasonKey>
            <#local apValue= partial.ap_value>
            <#local mpValue= partial.mp_value>
            <#if ! apValues?seq_contains(apValue)>
                <#local apValues = apValues + [apValue]>
            </#if>
            <#if ! mpValues?seq_contains(mpValue)>
                <#local mpValues = mpValues + [mpValue]>
            </#if>
        </#if>
    </#list>

    <#return { 'ap_values': apValues, 'mp_values': mpValues }>
</#function>

<#function getActivityDobComment ruleReason>
    <#local reducedReasonKey = ruleReason.reduced_reason_key>

    <#local partyValues = extractPartyValues(ruleReason)>
    <#local estimatedYear = extractEstimatedYear(ruleReason)>

    <#local apValues = partyValues['ap_values']?join(', ')>

    <#local comment = "Alerted Party's DOB/YOB (${apValues}) does not match Watchlist Party derived YOB">

    <#if (reducedReasonKey == "ap_probably_too_young" || reducedReasonKey == "ap_too_young") && estimatedYear['upper']?has_content>
        <#return "${comment}: #{estimatedYear['upper']; M0} or earlier.">
    <#elseif (reducedReasonKey == "ap_probably_too_old" || reducedReasonKey == "ap_too_old") && estimatedYear['upper']?has_content>
        <#return "${comment}: #{estimatedYear['lower']; M0} or later.">
    </#if>

    <!-- should not happen, only if watchlist information is internally contradictory -->
    <#return "${comment}.">
</#function>

<#function comment alertModel matchModel featureModel>
    <#local solution = featureModel.solution>

    <#local ruleReasons = featureModel.reason.rule_reasons>
    <#local comments = ["Logical YOB Discounting."]>
    <#list ruleReasons as ruleReason>
        <#local ruleSolution = ruleReason.reduced_solution>
        <#local ruleSolutionName = ruleSolution.name>
        <#local rule = ruleReason.rule>

        <#if ruleSolutionName == solution>
            <#if rule == 'activity_dob'>
                <#local comments = comments + [getActivityDobComment(ruleReason)]>
            </#if>
        </#if>
    </#list>

    <#return comments?join(" ")>
</#function>
