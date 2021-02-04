package com.silenteight.serp.governance.policy.startup;

import com.silenteight.serp.governance.policy.solve.InUsePolicyLoader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DefaultPolicyLoaderConfiguration {

  @Bean
  DefaultPolicyLoader defaultPolicyLoader(InUsePolicyLoader inUsePolicyLoader) {
    return new DefaultPolicyLoader(inUsePolicyLoader);
  }
}
