package com.silenteight.simulator.management.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.simulator.dataset.DatasetModule;
import com.silenteight.simulator.management.ManagementModule;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.create.TestAnalysisService;
import com.silenteight.simulator.management.progress.IndexedAlertProvider;

import io.grpc.Channel;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.simulator.management.SimulationFixtures.PROCESSING_TIMESTAMP_1;
import static java.time.Instant.parse;

@Configuration
@ComponentScan(basePackageClasses = {
    DatasetModule.class,
    ManagementModule.class
})
public class SimulationTestConfiguration {

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
  IndexedAlertProvider indexedAlertProvider() {
    return new TestIndexedAlertProvider();
  }

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP_1));
  }
}
