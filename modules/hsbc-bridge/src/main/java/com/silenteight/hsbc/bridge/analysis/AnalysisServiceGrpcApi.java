package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.hsbc.bridge.analysis.dto.*;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.silenteight.hsbc.bridge.common.util.TimestampUtil.toOffsetDateTime;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
class AnalysisServiceGrpcApi implements AnalysisServiceApi {

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
        .name(result.getName())
        .build();
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public AnalysisDto createAnalysis(CreateAnalysisRequestDto request) {
    var grpcRequest = CreateAnalysisRequest.newBuilder()
        .setAnalysis(Analysis.newBuilder()
            .setName(request.getName())
            .setStrategy(request.getStrategy())
            .setPolicy(request.getPolicy())
            .setAlertCount(request.getAlertCount())
            .setPendingAlerts(request.getPendingAlerts())
            .addAllFeatures(List.of())
            .addAllCategories(List.of())
            .build())
        .build();

    var result = getStub().createAnalysis(grpcRequest);
    return AnalysisDto.builder()
        .name(result.getName())
        // get more if needed
        .build();
  }

  @Override
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    var gprcRequest = StreamRecommendationsRequest.newBuilder()
        .setAnalysis(request.getAnalysis())
        .setDataset(request.getDataset())
        .build();
    var result = getStub().streamRecommendations(gprcRequest);

    var recommendations = new ArrayList<RecommendationDto>();
    result.forEachRemaining(item -> recommendations.add(mapRecommendation(item)));

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
