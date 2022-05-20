<#import "nested/nested.ftl" as nestedTemplate>

<#macro alert alertModel>
  The Alert ID is: ${alertModel.alertId}
  <@nestedTemplate.nested/><#t>
</#macro>
