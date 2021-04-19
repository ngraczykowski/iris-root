package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.changerequest.ChangeRequestModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ChangeRequestModule.class
})
class ChangeRequestTestConfiguration {

  @MockBean
  AuditingLogger auditingLogger;
}
