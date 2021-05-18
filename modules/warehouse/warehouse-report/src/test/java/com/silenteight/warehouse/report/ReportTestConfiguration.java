package com.silenteight.warehouse.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingModule;
import com.silenteight.warehouse.report.storage.StorageModule;
import com.silenteight.warehouse.report.synchronization.SynchronizationModule;
import com.silenteight.warehouse.report.tenant.TenantModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.*;

@Configuration
@ComponentScan(basePackageClasses = {
    OpendistroModule.class,
    ReportingModule.class,
    StorageModule.class,
    SynchronizationModule.class,
    TenantModule.class,
    TestElasticSearchModule.class,
})
@ImportAutoConfiguration({
    ElasticsearchRestClientAutoConfiguration.class,
    JacksonAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@RequiredArgsConstructor
public class ReportTestConfiguration {

  @Bean
  @Primary
  AnalysisService analysisService() {
    return mock(AnalysisService.class);
  }
}
