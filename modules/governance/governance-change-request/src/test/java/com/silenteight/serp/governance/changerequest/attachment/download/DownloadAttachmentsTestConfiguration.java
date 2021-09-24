package com.silenteight.serp.governance.changerequest.attachment.download;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.serp.governance.file.FileModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    DownloadAttachmentsConfiguration.class,
    FileModule.class,
    FileStorageMinioModule.class
})
public class DownloadAttachmentsTestConfiguration {

  @MockBean
  AuditingLogger auditingLogger;
}
