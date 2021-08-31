package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.Setter;

import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc;
import com.silenteight.serp.governance.common.grpc.GrpcCommonProperties;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateAlertWithDecisionUseCase;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AlertsGeneratorProperties.class)
class SamplingGeneratorConfiguration {

  @Setter(onMethod_ = @GrpcClient("warehouse"))
  private Channel warehouseChannel;

  @Bean
  AlertsGeneratorService alertsGeneratorService(
      DistributionProvider distributionProvider,
      AlertProvider alertProvider,
      CreateAlertWithDecisionUseCase createAlertWithDecisionUseCase,
      @Valid AlertsGeneratorProperties alertsGeneratorProperties,
      AlertSamplingService alertSamplingService,
      ApplicationEventPublisher applicationEventPublisher) {

    return new AlertsGeneratorService(
        distributionProvider,
        alertProvider,
        createAlertWithDecisionUseCase,
        alertsGeneratorProperties.getSampleCount(),
        alertsGeneratorProperties.getGroupingFields(),
        alertSamplingService,
        applicationEventPublisher);
  }

  @Bean
  DistributionProvider distributionProvider(
      @Valid GrpcCommonProperties grpcCommonProperties) {

    return new DistributionProvider(
        DistributionAlertsServiceGrpc.newBlockingStub(warehouseChannel).withWaitForReady(),
        grpcCommonProperties.getTimoutMillis());
  }

  @Bean
  AlertProvider alertProvider(
      @Valid GrpcCommonProperties grpcCommonProperties) {

    return new AlertProvider(
        SamplingAlertsServiceGrpc.newBlockingStub(warehouseChannel).withWaitForReady(),
        grpcCommonProperties.getTimoutMillis());
  }
}
