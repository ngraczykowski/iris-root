package com.silenteight.warehouse.sampling.distribution;

import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({ SamplingProperties.class })
public class DistributionConfiguration {

  @Bean
  DistributionAlertsService distributionAlertsService(
      GroupingQueryService groupingQueryService,
      @Valid SamplingProperties samplingProperties) {

    return new DistributionAlertsService(
        groupingQueryService,
        samplingProperties);
  }

  @Bean
  @GrpcService
  DistributionAlertGrpcService distributionAlertGrpcService(
      DistributionAlertsService distributionAlertsService) {

    return new DistributionAlertGrpcService(distributionAlertsService);
  }
}
