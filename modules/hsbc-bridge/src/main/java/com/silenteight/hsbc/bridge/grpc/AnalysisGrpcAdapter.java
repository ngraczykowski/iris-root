package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.hsbc.bridge.analysis.dto.*;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
class AnalysisGrpcAdapter implements AnalysisServiceClient {

  private final AnalysisServiceBlockingStub analysisServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public AnalysisDatasetDto addDataset(AddDatasetRequestDto request) {
    var grpcRequest = AddDatasetRequest.newBuilder()
        .setAnalysis(request.getAnalysis())
        .setDataset(request.getDataset())
        .build();

    var result = getStub().addDataset(grpcRequest);
    return AnalysisDatasetDto.builder()
        .alertsCount(result.getAlertCount())
        .name(result.getName())
        .build();
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public CreateAnalysisResponseDto createAnalysis(CreateAnalysisRequestDto request) {
    var grpcRequest = CreateAnalysisRequest.newBuilder()
        .setAnalysis(Analysis.newBuilder()
            .setStrategy(request.getStrategy())
            .setPolicy(request.getPolicy())
            .addAllCategories(request.getCategories())
            .addAllFeatures(mapFeatures(request.getFeatures()))
            .build())
        .build();

    var result = getStub().createAnalysis(grpcRequest);
    return CreateAnalysisResponseDto.builder()
        .name(result.getName())
        .policy(result.getPolicy())
        .strategy(result.getStrategy())
        .build();
  }

  private List<Feature> mapFeatures(List<FeatureDto> features) {
    return features.stream()
        .map(f -> Feature.newBuilder()
            .setAgentConfig(f.getAgentConfig())
            .setFeature(f.getName())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public GetAnalysisResponseDto getAnalysis(String analysis) {
    var request = GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();

    var result = getStub().getAnalysis(request);
    return GetAnalysisResponseDto.builder()
        .alertsCount(result.getAlertCount())
        .pendingAlerts(result.getPendingAlerts())
        .build();
  }

  private AnalysisServiceBlockingStub getStub() {
    return analysisServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
