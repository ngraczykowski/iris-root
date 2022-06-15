<#import "match.ftl" as matchTemplate>

<#macro match matchModel>
    <@matchTemplate.match matchModel matchModel/>
</#macro>

<@match .data_model/>
