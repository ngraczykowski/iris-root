package com.silenteight.warehouse.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingModule;
import com.silenteight.warehouse.report.simulation.SimulationModule;
import com.silenteight.warehouse.report.storage.StorageModule;
import com.silenteight.warehouse.report.synchronization.SynchronizationModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.*;

@Configuration
@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    OpendistroModule.class,
    ReportingModule.class,
    SimulationModule.class,
    StorageModule.class,
    SynchronizationModule.class,
    TestElasticSearchModule.class,
    TokenModule.class,
    TimeModule.class
})
@ImportAutoConfiguration({
    JacksonAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@RequiredArgsConstructor
public class ProductionPeriodicTestConfiguration {

  @Bean
  @Primary
  SimulationAnalysisService analysisService() {
    return mock(SimulationAnalysisService.class);
  }
}
