package com.silenteight.warehouse.sampling.alert;


import com.silenteight.warehouse.indexer.alert.RandomAlertQueryService;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SamplingConfiguration {

  @Bean
  SamplingAlertService samplingAlertService(RandomAlertQueryService randomAlertQueryService) {
    return new SamplingAlertService(randomAlertQueryService);
  }

  @Bean
  @GrpcService
  SamplingAlertGrpcService samplingAlertGrpcService(SamplingAlertService samplingAlertService) {
    return new SamplingAlertGrpcService(samplingAlertService);
  }
}
