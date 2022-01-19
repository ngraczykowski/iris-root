package com.silenteight.warehouse.report;


import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.query.QueryAlertModule;
import com.silenteight.warehouse.report.reporting.ReportingModule;
import com.silenteight.warehouse.report.sql.SqlExecutorModule;
import com.silenteight.warehouse.report.storage.StorageModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    OpendistroModule.class,
    ReportingModule.class,
    QueryAlertModule.class,
    StorageModule.class,
    TestElasticSearchModule.class,
    TokenModule.class,
    FileStorageMinioModule.class,
    SqlExecutorModule.class,
    DomainModule.class
})
class ReportGenerationConfiguration  {
}
