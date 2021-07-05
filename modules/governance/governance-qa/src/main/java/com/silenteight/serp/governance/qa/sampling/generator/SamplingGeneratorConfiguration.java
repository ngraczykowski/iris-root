package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.Setter;

import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateDecisionUseCase;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AlertsGeneratorProperties.class)
public class SamplingGeneratorConfiguration {

  @Setter(onMethod_ = @GrpcClient("warehouse"))
  private Channel warehouseChannel;

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
  DistributionProvider distributionProvider() {
    return new DistributionProvider(
        DistributionAlertsServiceGrpc
            .newBlockingStub(warehouseChannel)
            .withWaitForReady());
  }

  @Bean
  AlertProvider alertProvider() {
    return new AlertProvider(
        SamplingAlertsServiceGrpc
            .newBlockingStub(warehouseChannel)
            .withWaitForReady());
  }
}
