package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceClient;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.hsbc.bridge.analysis.dto.*;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.common.util.TimestampUtil.toOffsetDateTime;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
class AnalysisGrpcAdapter implements AnalysisServiceClient, RecommendationServiceClient {

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

  @Override
  @Retryable(value = CannotGetRecommendationsException.class)
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    var recommendations = new ArrayList<RecommendationDto>();
    var grpcRequest = StreamRecommendationsRequest.newBuilder()
        .setAnalysis(request.getAnalysis())
        .build();

    try {
      getStub().streamRecommendations(grpcRequest)
          .forEachRemaining(item -> recommendations.add(mapRecommendation(item)));
    } catch (StatusRuntimeException ex) {
      log.error("Cannot get recommendations", ex);
      throw new CannotGetRecommendationsException();
    }

    return recommendations;
  }

  private RecommendationDto mapRecommendation(Recommendation recommendation) {
    return RecommendationDto.builder()
        .alert(recommendation.getAlert())
        .name(recommendation.getName())
        .date(toOffsetDateTime(recommendation.getCreateTimeOrBuilder()))
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .build();
  }

  private AnalysisServiceBlockingStub getStub() {
    return analysisServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
