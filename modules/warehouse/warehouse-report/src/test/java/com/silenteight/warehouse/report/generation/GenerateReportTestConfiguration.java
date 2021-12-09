package com.silenteight.warehouse.report.generation;

import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.warehouse.report.sql.SqlExecutorModule;
import com.silenteight.warehouse.report.storage.StorageModule;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackageClasses = {
    SqlExecutorModule.class,
    StorageModule.class,
    FileStorageMinioModule.class,
    GenerateReportModule.class
})
@Configuration
@EnableJpaRepositories(basePackages = "com.silenteight.warehouse")
@EntityScan(basePackages = "com.silenteight.warehouse")
class GenerateReportTestConfiguration {
}
