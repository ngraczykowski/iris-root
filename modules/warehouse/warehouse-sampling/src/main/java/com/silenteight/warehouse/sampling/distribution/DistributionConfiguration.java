package com.silenteight.warehouse.sampling.distribution;


import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributionConfiguration {

  @Bean
  @GrpcService
  DistributionAlertGrpcService distributionAlertGrpcService() {
    return new DistributionAlertGrpcService();
  }
}
