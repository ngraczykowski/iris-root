package com.silenteight.warehouse.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingModule;
import com.silenteight.warehouse.report.simulation.SimulationModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    OpendistroModule.class,
    ReportingModule.class,
    SimulationModule.class,
    TestElasticSearchModule.class,
    TokenModule.class,
    TimeModule.class
})
@RequiredArgsConstructor
public class SimulationTestConfiguration {

  @Bean
  @Primary
  SimulationAnalysisService simulationAnalysisService() {
    return mock(SimulationAnalysisService.class);
  }
}
