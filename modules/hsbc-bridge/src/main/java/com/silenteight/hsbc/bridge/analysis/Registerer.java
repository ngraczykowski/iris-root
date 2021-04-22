package com.silenteight.hsbc.bridge.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.dto.*;
import com.silenteight.hsbc.bridge.model.ModelUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class Registerer {

  private final AnalysisServiceApi analysisServiceApi;
  private final ModelUseCase modelUseCase;

  AnalysisDto registerAnalysis(@NonNull String dataset) {
    var createAnalysisResponse = createAnalysis();
    var analysis = addDatasetToAnalysis(createAnalysisResponse, dataset);

    log.info("Analysis: {} created", analysis);
    return analysis;
  }

  private CreateAnalysisResponseDto createAnalysis() {
    var solvingModel = modelUseCase.getSolvingModel();
    var request = CreateAnalysisRequestDto.builder()
        .policy(solvingModel.getPolicyName())
        .strategy(solvingModel.getStrategyName())
        .features(map(solvingModel.getFeatures()))
        .build();

    return analysisServiceApi.createAnalysis(request);
  }

  private static List<FeatureDto> map(List<com.silenteight.hsbc.bridge.model.FeatureDto> features) {
    return features.stream().map(f ->
        FeatureDto.builder()
            .name(f.getName())
            .agentConfig(f.getAgentConfig())
            .build())
        .collect(Collectors.toList());
  }

  private AnalysisDto addDatasetToAnalysis(CreateAnalysisResponseDto response, String dataset) {
    var addDatasetResponse = analysisServiceApi.addDataset(
        AddDatasetRequestDto.builder()
            .analysis(response.getName())
            .dataset(dataset)
            .build());

    return AnalysisDto.builder()
        .alertCount(addDatasetResponse.getAlertsCount())
        .dataset(dataset)
        .name(response.getName())
        .policy(response.getPolicy())
        .strategy(response.getStrategy())
        .build();
  }
}
