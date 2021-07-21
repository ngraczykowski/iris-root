package com.silenteight.simulator.management.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.AddDatasetRequest;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.adjudication.api.v1.GetAnalysisRequest;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.management.create.AnalysisService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class GrpcAnalysisService implements AnalysisService {

  @NonNull
  private final AnalysisServiceBlockingStub analysisStub;

  @Override
  public Analysis createAnalysis(SolvingModel model) {
    log.debug("Creating Analysis with model={}", model);

    return analysisStub.createAnalysis(toCreateAnalysisRequest(model));
  }

  private static CreateAnalysisRequest toCreateAnalysisRequest(SolvingModel model) {
    return CreateAnalysisRequest.newBuilder()
        .setAnalysis(toAnalysis(model))
        .build();
  }

  private static Analysis toAnalysis(SolvingModel model) {
    return Analysis.newBuilder()
        .setPolicy(model.getPolicyName())
        .setStrategy(model.getStrategyName())
        .addAllCategories(model.getCategoriesList())
        .addAllFeatures(toFeatures(model.getFeaturesList()))
        .build();
  }

  private static List<Feature> toFeatures(
      List<com.silenteight.model.api.v1.Feature> features) {

    return features
        .stream()
        .map(GrpcAnalysisService::toFeature)
        .collect(toList());
  }

  private static Feature toFeature(com.silenteight.model.api.v1.Feature feature) {
    return Feature.newBuilder()
        .setFeature(feature.getName())
        .setAgentConfig(feature.getAgentConfig())
        .build();
  }

  @Override
  public void addDatasetToAnalysis(String analysis, String dataset) {
    log.debug("Adding dataset={} to analysis={}", analysis, dataset);

    analysisStub.addDataset(toAddDatasetRequest(analysis, dataset));
  }

  private static AddDatasetRequest toAddDatasetRequest(String analysis, String dataset) {
    return AddDatasetRequest.newBuilder()
        .setAnalysis(analysis)
        .setDataset(dataset)
        .build();
  }

  @Override
  public Analysis getAnalysis(String analysis) {
    log.debug("Getting analysis by name={}", analysis);

    return analysisStub.getAnalysis(toGetAnalysisRequest(analysis));
  }

  private static GetAnalysisRequest toGetAnalysisRequest(String analysis) {
    return GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();
  }
}
