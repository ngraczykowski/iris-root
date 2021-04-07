package com.silenteight.simulator.management;

import com.silenteight.auditing.bs.AuditingLogger;

import io.grpc.Channel;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ManagementModule.class)
class ManagementTestConfiguration {

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
}
