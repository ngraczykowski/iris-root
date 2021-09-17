package com.silenteight.warehouse.sampling.alert;

import com.silenteight.warehouse.indexer.query.single.RandomAlertQueryService;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SamplingProperties.class)
public class SamplingConfiguration {

  @Bean
  SamplingAlertService samplingAlertService(
      RandomAlertQueryService randomAlertQueryService,
      SamplingProperties samplingProperties) {

    return new SamplingAlertService(randomAlertQueryService, samplingProperties);
  }

  @Bean
  @GrpcService
  SamplingAlertGrpcService samplingAlertGrpcService(SamplingAlertService samplingAlertService) {
    return new SamplingAlertGrpcService(samplingAlertService);
  }
}
