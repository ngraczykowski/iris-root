package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateDecisionUseCase;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AlertsGeneratorProperties.class)
public class SamplingGeneratorConfiguration {

  @Bean
  AlertsGeneratorService alertsGeneratorService(
      DistributionProvider distributionProvider,
      AlertProvider alertProvider,
      CreateDecisionUseCase createDecisionUseCase,
      @Valid AlertsGeneratorProperties alertsGeneratorProperties,
      AlertSamplingService alertSamplingService) {

    return new AlertsGeneratorService(
        distributionProvider,
        alertProvider,
        createDecisionUseCase,
        alertsGeneratorProperties.getSampleCount(),
        GroupingFields.valuesAsStringList(),
        alertSamplingService);
  }

  @Bean
  DistributionProvider distributionProvider(@Qualifier("governance") Channel channel) {
    return new DistributionProvider(
        DistributionAlertsServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }

  @Bean
  AlertProvider alertProvider(@Qualifier("governance") Channel channel) {
    return new AlertProvider(
        SamplingAlertsServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
