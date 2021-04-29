package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;
import com.silenteight.hsbc.bridge.transfer.TransferClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
@RequiredArgsConstructor
@EnableConfigurationProperties({
    AlertGrpcAdapterProperties.class,
    AnalysisGrpcAdapterProperties.class,
    DatasetGrpcAdapterProperties.class,
    TransferModelGrpcAdapterProperties.class })
class GrpcServiceConfiguration {

  private final AlertGrpcAdapterProperties alertGrpcAdapterProperties;
  private final AnalysisGrpcAdapterProperties analysisGrpcAdapterProperties;
  private final DatasetGrpcAdapterProperties datasetGrpcAdapterProperties;
  private final TransferModelGrpcAdapterProperties transferModelGrpcAdapterProperties;

  @Bean
  AnalysisGrpcAdapter analysisServiceApi() {
    return new AnalysisGrpcAdapter(
        analysisServiceBlockingStub(), analysisGrpcAdapterProperties.getDeadlineInSeconds());
  }

  private AnalysisServiceBlockingStub analysisServiceBlockingStub() {

    return AnalysisServiceGrpc.newBlockingStub(
        getManagedChannel(analysisGrpcAdapterProperties.getGrpcAddress()))
        .withWaitForReady();
  }

  @Bean
  AlertGrpcAdapter alertServiceGrpcApi() {
    return new AlertGrpcAdapter(
        alertServiceBlockingStub(), alertGrpcAdapterProperties.getDeadlineInSeconds());
  }

  private AlertServiceGrpc.AlertServiceBlockingStub alertServiceBlockingStub() {

    return AlertServiceGrpc.newBlockingStub(
        getManagedChannel(alertGrpcAdapterProperties.getGrpcAddress()))
        .withWaitForReady();
  }

  @Bean
  DatasetServiceClient datasetServiceApiGrpc() {
    return new DatasetGrpcAdapter(
        datasetServiceBlockingStub(), datasetGrpcAdapterProperties.getDeadlineInSeconds());
  }

  private DatasetServiceGrpc.DatasetServiceBlockingStub datasetServiceBlockingStub() {

    return DatasetServiceGrpc.newBlockingStub(
        getManagedChannel(datasetGrpcAdapterProperties.getGrpcAddress()))
        .withWaitForReady();
  }

  private ManagedChannel getManagedChannel(String address) {
    return ManagedChannelBuilder.forTarget(address)
        .usePlaintext()
        .build();
  }

  @Bean
  TransferClient transferModelGrpcApi() {
    // TODO waiting for .proto file from Governance !
    return new TransferModelGrpcAdapter(transferModelGrpcAdapterProperties.getDeadlineInSeconds());
  }
}
