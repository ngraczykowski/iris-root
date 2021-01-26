package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.analytics.StoreFeatureVectorSolvedUseCase;
import com.silenteight.serp.governance.policy.PolicyModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    PolicyModule.class
})
class PolicyRepositoryTestConfiguration {


  @MockBean
  private AuditingLogger auditingLogger;

  // TODO(anowicki): WEB-479 - remove dependency on analytics
  @MockBean
  private StoreFeatureVectorSolvedUseCase handler;
}
