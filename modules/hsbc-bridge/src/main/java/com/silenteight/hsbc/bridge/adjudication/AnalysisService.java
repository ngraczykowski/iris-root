package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AddDatasetRequest;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AnalysisService {

  private final AnalysisServiceBlockingStub analysisServiceBlockingStub;

  String createAnalysisWithDataset(
      SolvingModelDto solvingModel, int alertsCount, String datasetId) {
    List<Feature> features = solvingModel.getFeatures().stream().map(f -> {
      return Feature.newBuilder()
          .setFeature(f.getName())
          .setAgentConfig(f.getAgentConfig())
          .build();
    }).collect(Collectors.toList());

    Analysis analysis = Analysis.newBuilder()
        .setName(UUID.randomUUID().toString())
        .setPolicy(solvingModel.getPolicyName())
        .setStrategy(solvingModel.getStrategyName())
        .addAllFeatures(features)
        .setPendingAlerts(alertsCount)
        .setAlertCount(alertsCount)
        .build();

    CreateAnalysisRequest analysisRequest = CreateAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();

    Analysis analysisResponse = analysisServiceBlockingStub.createAnalysis(analysisRequest);

    AddDatasetRequest addDatasetRequest = AddDatasetRequest.newBuilder()
        .setAnalysis(analysisResponse.getName())
        .setDataset(datasetId)
        .build();

    analysisServiceBlockingStub.addDataset(addDatasetRequest);

    return analysisResponse.getName();
  }
}
