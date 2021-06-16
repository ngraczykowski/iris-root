package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.common.signature.SignatureModule;
import com.silenteight.serp.governance.policy.PolicyModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    PolicyModule.class,
    SignatureModule.class
})
class PolicyRepositoryTestConfiguration {

  @MockBean
  private AuditingLogger auditingLogger;
}
