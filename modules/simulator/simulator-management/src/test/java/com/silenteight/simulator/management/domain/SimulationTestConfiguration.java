package com.silenteight.simulator.management.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.ManagementModule;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.create.TestAnalysisService;

import io.grpc.Channel;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ManagementModule.class)
class SimulationTestConfiguration {

  @Bean
  AuditingLogger auditingLogger() {
    return Mockito.mock(AuditingLogger.class);
  }

  @Bean("adjudication-engine")
  Channel adjudicationEngineChannel() {
    return Mockito.mock(Channel.class);
  }

  @Bean("governance")
  Channel governanceChannel() {
    return Mockito.mock(Channel.class);
  }

  @Bean
  AnalysisService analysisService() {
    return new TestAnalysisService();
  }

  @Bean
  DatasetQuery datasetQuery() {
    return Mockito.mock(DatasetQuery.class);
  }
}
