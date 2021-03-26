package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdjudicationGrpcConfiguration {

  private final AdjudicationProperties adjudicationProperties;

  @Bean
  AlertServiceBlockingStub alertServiceBlockingStub() {
    ManagedChannel channel = createManagedChannel();

    return AlertServiceGrpc.newBlockingStub(channel).withWaitForReady();
  }

  @Bean
  AnalysisServiceBlockingStub analysisServiceBlockingStub() {
    ManagedChannel channel = createManagedChannel();

    return AnalysisServiceGrpc.newBlockingStub(channel).withWaitForReady();
  }

  @Bean
  DatasetServiceBlockingStub datasetServiceBlockingStub() {
    ManagedChannel channel = createManagedChannel();

    return DatasetServiceGrpc.newBlockingStub(channel).withWaitForReady();
  }

  private ManagedChannel createManagedChannel() {
    return ManagedChannelBuilder.forTarget(adjudicationProperties.getAddress())
        .usePlaintext()
        .build();
  }
}
