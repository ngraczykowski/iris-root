package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.AddDatasetRequest;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.adjudication.api.v1.GetAnalysisRequest;

import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
public class AnalysisGrpcAdapter implements AnalysisServiceClient {

  private final AnalysisServiceBlockingStub analysisServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public AnalysisDatasetOut addDataset(AddDatasetIn request) {
    var grpcRequest = AddDatasetRequest.newBuilder()
        .setAnalysis(request.getAnalysis())
        .setDataset(request.getDataset())
        .build();

    try {
      var result = getStub().addDataset(grpcRequest);

      return AnalysisDatasetOut.builder()
          .alertsCount(result.getAlertCount())
          .name(result.getName())
          .build();
    } catch (StatusRuntimeException e) {
      log.error("Cannot add dataset", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot add dataset", e);
    }
  }

  @Override
  public CreateAnalysisOut createAnalysis(CreateAnalysisIn request) {
    var grpcRequest = CreateAnalysisRequest.newBuilder()
        .setAnalysis(Analysis.newBuilder()
            .setStrategy(request.getStrategy())
            .setPolicy(request.getPolicy())
            .addAllCategories(request.getCategories())
            .addAllFeatures(mapFeatures(request.getFeatures()))
            .build())
        .build();

    try {
      var result = getStub().createAnalysis(grpcRequest);

      return CreateAnalysisOut.builder()
          .name(result.getName())
          .policy(result.getPolicy())
          .strategy(result.getStrategy())
          .build();
    } catch (StatusRuntimeException e) {
      log.error("Cannot create analysis", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot create analysis", e);
    }
  }

  private List<Feature> mapFeatures(List<FeatureIn> features) {
    return features.stream()
        .map(f -> Feature.newBuilder()
            .setAgentConfig(f.getAgentConfig())
            .setFeature(f.getName())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public GetAnalysisOut getAnalysis(String analysis) {
    var grpcRequest = GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();

    try {
      var result = getStub().getAnalysis(grpcRequest);

      return GetAnalysisOut.builder()
          .alertsCount(result.getAlertCount())
          .pendingAlerts(result.getPendingAlerts())
          .build();
    } catch (StatusRuntimeException e) {
      log.error("Cannot get analysis", e);
      throw new AdjudicationEngineLibraryRuntimeException("Cannot get analysis", e);
    }
  }

  private AnalysisServiceBlockingStub getStub() {
    return analysisServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
