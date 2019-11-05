package com.silenteight.sens.webapp.security.acl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DomainObjectIdentityGeneratorConfiguration {

  @Bean
  DecisionTreeIdDomainObjectIdentityGenerator reasoningBranchIdDomainObjectIdentityGenerator() {
    return new DecisionTreeIdDomainObjectIdentityGenerator();
  }

  @Bean
  WorkflowLevelDomainObjectIdentityGenerator workflowLevelDomainObjectIdentityGenerator() {
    return new WorkflowLevelDomainObjectIdentityGenerator();
  }
}
