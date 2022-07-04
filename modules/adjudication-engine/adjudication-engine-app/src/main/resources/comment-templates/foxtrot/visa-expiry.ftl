<#function isSolutionConsistent alertModel matchModel groupContext>
    <#if isFalsePositiveCase(groupContext) && matchModel.solution?matches('.*FALSE_POSITIVE')>
        <#return true>
    <#elseif isPotentialTruePositiveWithoutUaeNationalIdCase(groupContext) && matchModel.solution?matches('.*TRUE_POSITIVE')>
        <#return true>
    <#elseif isPotentialTruePositiveWithUaeNationalIdCase(groupContext) && matchModel.solution?matches('.*TRUE_POSITIVE')>
        <#return true>

    </#if>
</#function>

<#function shouldAdd alertModel matchModel groupContext>
    <#if !matchModel.reason.step_title?matches(".*visa expiry.*", 'i')>
        <#return false>
    <#elseif !isSolutionConsistent(alertModel, matchModel, groupContext)>
        <#return false>
    </#if>

    <#return true>
</#function>

<#function comment alertModel matchModel groupContext>
    <#if isFalsePositiveCase(groupContext)>
        <#return "Hit is against the nationality of the customer and there is no hit against the customer name. "
        + "As per the T24, customer is currently resident in UAE and holds a valid VISA.">
    <#elseif isPotentialTruePositiveWithoutUaeNationalIdCase(groupContext)>
        <#return "Hit is against the nationality of the customer and there is no hit against the customer name. "
        + "As per the T24, no valid documents were provided, hence considered as non-resident.">
    <#elseif isPotentialTruePositiveWithUaeNationalIdCase(groupContext)>
        <#return "Hit is against the nationality of the customer and there is no hit against the customer name. "
        + "As per the T24, customer does not hold a valid document number (invalid date), hence considered as potential non-resident.">
    </#if>>

    <#stop "Unsupported case for visa expiry feature.">
</#function>


<#function isFalsePositiveCase groupContext>
    <#if getHitType(groupContext) == "SAN"
    && getWatchlistType(groupContext) == "ADDRESS"
    && getIsHitOnWlName(groupContext) == "NO"
    && getRecordSourceType(groupContext) == "T24"
    && getIsUaeNationalId(groupContext) == "TRUE"
    && ["AFTER"]?seq_contains(getVisaExpiryDateVsToday(groupContext))>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function isPotentialTruePositiveWithoutUaeNationalIdCase groupContext>
    <#if getHitType(groupContext) == "SAN"
    && getWatchlistType(groupContext) == "ADDRESS"
    && getIsHitOnWlName(groupContext) == "NO"
    && getRecordSourceType(groupContext) == "T24"
    && ["FALSE", "NO_DATA", "INCONCLUSIVE"]?seq_contains(getIsUaeNationalId(groupContext))>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function isPotentialTruePositiveWithUaeNationalIdCase groupContext>
    <#if getHitType(groupContext) == "SAN"
    && getWatchlistType(groupContext) == "ADDRESS"
    && getIsHitOnWlName(groupContext) == "NO"
    && getRecordSourceType(groupContext) == "T24"
    && getIsUaeNationalId(groupContext) == "TRUE"
    && ["BEFORE", "EQUAL", "INCONCLUSIVE", "AGENT_ERROR"]?seq_contains(getVisaExpiryDateVsToday(groupContext))>
        <#return true>
    </#if>

    <#return false>
</#function>

<#function getHitType groupContext>
    <#return getCategory(groupContext, "hitType")>
</#function>

<#function getWatchlistType groupContext>
    <#return getCategory(groupContext, "watchlistType")>
</#function>

<#function getIsHitOnWlName groupContext>
    <#return getCategory(groupContext, "isHitOnWlName")>
</#function>

<#function getRecordSourceType groupContext>
    <#return getCategory(groupContext, "recordSourceType")>
</#function>

<#function getIsUaeNationalId groupContext>
    <#return getFeature(groupContext, "isUaeNationalId")>
</#function>

<#function getVisaExpiryDateVsToday groupContext>
    <#return getFeature(groupContext, "visaExpiryDateVsToday")>
</#function>

<#function getFeature groupContext name>
    <#local features = groupContext["features"]>
    <#return features
    ?filter(f -> f["name"] == name)
    ?map(f -> f["solution"])[0]!"">
</#function>

<#function getCategory groupContext name>
    <#local features = groupContext["categories"]>
    <#return features
    ?filter(f -> f["name"] == name)
    ?map(f -> f["value"])[0]!"">
</#function>
