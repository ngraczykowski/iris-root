package com.silenteight.simulator.management.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.simulator.management.ManagementModule;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.create.CreateSimulationUseCase;
import com.silenteight.simulator.management.create.ModelService;
import com.silenteight.simulator.management.progress.IndexedAlertProvider;
import com.silenteight.simulator.management.progress.SimulationProgressUseCase;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.simulator.management.SimulationFixtures.PROCESSING_TIMESTAMP_1;
import static java.time.Instant.parse;

@Configuration
@ComponentScan(basePackageClasses = ManagementModule.class)
public class SimulationTestConfiguration {

  @MockBean
  AuditingLogger auditingLogger;

  @MockBean(name = "grpcModelService")
  ModelService grpcModelService;

  @MockBean(name = "grpcAnalysisService")
  AnalysisService grpcAnalysisService;

  @MockBean
  CreateSimulationUseCase createSimulationUseCase;

  @MockBean
  SimulationProgressUseCase simulationProgressUseCase;

  @Bean
  IndexedAlertProvider indexedAlertProvider() {
    return new TestIndexedAlertProvider();
  }

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP_1));
  }
}
