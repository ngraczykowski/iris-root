package com.silenteight.warehouse.report;

import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.report.create.ReportCreateModule;
import com.silenteight.warehouse.report.download.ReportDownloadModule;
import com.silenteight.warehouse.report.generation.ReportGenerationModule;
import com.silenteight.warehouse.report.name.ReportFileNameModule;
import com.silenteight.warehouse.report.persistence.ReportPersistenceModule;
import com.silenteight.warehouse.report.sql.SqlExecutorModule;
import com.silenteight.warehouse.report.status.ReportStatusModule;
import com.silenteight.warehouse.report.storage.StorageModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    DomainModule.class,
    FileStorageMinioModule.class,
    ReportCreateModule.class,
    ReportDownloadModule.class,
    ReportFileNameModule.class,
    ReportGenerationModule.class,
    ReportPersistenceModule.class,
    ReportStatusModule.class,
    StorageModule.class,
    SqlExecutorModule.class
})
class ReportGenerationConfiguration {
}
