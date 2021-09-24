package com.silenteight.serp.governance.changerequest.attachment.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.serp.governance.changerequest.ChangeRequestModule;
import com.silenteight.serp.governance.file.FileModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ChangeRequestModule.class,
    FileModule.class,
    FileStorageMinioModule.class
})
class ChangeRequestTestConfiguration {

  @MockBean
  AuditingLogger auditingLogger;
}
