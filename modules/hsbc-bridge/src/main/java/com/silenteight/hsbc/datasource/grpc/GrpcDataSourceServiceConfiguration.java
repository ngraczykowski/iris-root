package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.KnownServices;
import com.silenteight.hsbc.bridge.grpc.GrpcProperties;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.worldcheck.api.v1.IsPepServiceGrpc.IsPepServiceBlockingStub;
import com.silenteight.worldcheck.api.v1.NamesInformationServiceGrpc.NamesInformationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
@RequiredArgsConstructor
@EnableConfigurationProperties({ GrpcProperties.class })
class GrpcDataSourceServiceConfiguration {

  private final GrpcProperties grpcProperties;

  @GrpcClient(KnownServices.WORLDCHECK)
  private IsPepServiceBlockingStub isPepServiceBlockingStub;

  @GrpcClient(KnownServices.WORLDCHECK)
  private NamesInformationServiceBlockingStub namesInformationServiceBlockingStub;

  @Bean
  IsPepServiceClient isPepInformationServiceGrpcApi() {
    return new IsPepGrpcAdapter(
        isPepServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }

  @Bean
  NameInformationServiceClient nameInformationServiceGrpcApi() {
    return new NameInformationGrpcAdapter(
        namesInformationServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }
}
