<#-- General comment for solution = noData -->
<#function noData customerAttribute, watchlistAttribute, customerValue, watchlistValue>
    <#return "Empty value is ignored">
</#function>

<#-- General comment for solution = inconclusive -->
<#function inconclusive customerAttribute, watchlistAttribute, customerValue, watchlistValue>
    <#if customerValue?trim?has_content>
        <#if watchlistValue?trim?has_content>
            <#return "Alerted Party's " +  customerAttribute + " ("+ customerValue + ") got matched with Watchlist Party " + watchlistAttribute + " (" + watchlistValue?trim + ")." >
        <#else>
            <#return "Alerted Party's " +  customerAttribute + " ("+ customerValue + ") got matched with no Watchlist Party "+ watchlistAttribute + "." >
        </#if>
    <#else>
        <#return "Alerted Party's " + customerAttribute + " could not be resolved" />
    </#if>
</#function>

<#-- General comment for solution is unexpected -->
<#function unexpected solution, customerAttribute, watchlistAttribute, customerValue, watchlistValue>
    <#if solution != 'AGENT_SKIPPED'>
      throwException('Unexpected solution: ' + solution)
    </#if>
    <#return "">
</#function>

<#-- General comment with solution mapping -->
<#function simpleComment solution customerAttribute watchlistAttribute customerValue watchlistValue solutionTextMappings={}>
    <#if solutionTextMappings[solution]??>
        <#return "Alerted Party's ${customerAttribute} (${customerValue}) ${solutionTextMappings[solution]} Watchlist Party ${watchlistAttribute} (${watchlistValue}).">
    <#elseif solution == "INCONCLUSIVE">
        <#return "${inconclusive(customerAttribute, watchlistAttribute, customerValue, watchlistValue)}">
    <#elseif solution == "NO_DATA">
        <#return "${noData(customerAttribute, watchlistAttribute, customerValue, watchlistValue)}">
    <#else>
        <#return "${unexpected(solution, customerAttribute, watchlistAttribute, customerValue, watchlistValue)}">
    </#if>
</#function>
