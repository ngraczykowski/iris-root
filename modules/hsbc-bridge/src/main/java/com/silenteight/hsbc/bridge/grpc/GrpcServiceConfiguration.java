package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.hsbc.bridge.KnownServices;
import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;
import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
@RequiredArgsConstructor
@EnableConfigurationProperties({
    GrpcProperties.class
})
class GrpcServiceConfiguration {

  private final GrpcProperties grpcProperties;

  @GrpcClient(KnownServices.ADJUDICATION_ENGINE)
  private AnalysisServiceBlockingStub analysisServiceBlockingStub;

  @GrpcClient(KnownServices.ADJUDICATION_ENGINE)
  private AlertServiceBlockingStub alertServiceBlockingStub;

  @GrpcClient(KnownServices.ADJUDICATION_ENGINE)
  private DatasetServiceBlockingStub datasetServiceBlockingStub;

  @GrpcClient(KnownServices.GOVERNANCE)
  private SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  @Bean
  AnalysisGrpcAdapter analysisServiceGrpcApi() {
    return new AnalysisGrpcAdapter(analysisServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }

  @Bean
  AlertGrpcAdapter alertServiceGrpcApi() {
    return new AlertGrpcAdapter(alertServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }

  @Bean
  DatasetServiceClient datasetServiceGrpcApi() {
    return new DatasetGrpcAdapter(datasetServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }

  @Bean
  ModelServiceClient modelServiceGrpcApi() {
    return new ModelGrpcAdapter(
        solvingModelServiceBlockingStub, grpcProperties.deadlineInSeconds());
  }
}
