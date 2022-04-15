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
    CreateAnalysisRequest createAnalysisRequest = toCreateAnalysisRequest(model);
    log.debug("Creating Analysis with model={}", model);
    Analysis analysis = analysisStub.createAnalysis(createAnalysisRequest);
    log.debug("Created Analysis with model={}", model);
    return analysis;
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
    AddDatasetRequest addDatasetRequest = toAddDatasetRequest(analysis, dataset);
    log.debug("Adding dataset={} to analysis={}", analysis, dataset);
    analysisStub.addDataset(addDatasetRequest);
    log.debug("Added dataset={} to analysis={}", analysis, dataset);
  }

  private static AddDatasetRequest toAddDatasetRequest(String analysis, String dataset) {
    return AddDatasetRequest.newBuilder()
        .setAnalysis(analysis)
        .setDataset(dataset)
        .build();
  }

  @Override
  public Analysis getAnalysis(String analysisName) {
    GetAnalysisRequest getAnalysisRequest = toGetAnalysisRequest(analysisName);
    log.debug("Getting analysis by name={}", analysisName);
    Analysis analysis  = analysisStub.getAnalysis(getAnalysisRequest);
    log.debug("Got analysis by name={}", analysisName);
    return analysis;
  }

  private static GetAnalysisRequest toGetAnalysisRequest(String analysis) {
    return GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();
  }
}
