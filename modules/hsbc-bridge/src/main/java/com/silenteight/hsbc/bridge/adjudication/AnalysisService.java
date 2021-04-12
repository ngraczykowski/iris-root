package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;
import com.silenteight.hsbc.bridge.analysis.dto.AddDatasetRequestDto;
import com.silenteight.hsbc.bridge.analysis.dto.CreateAnalysisRequestDto;
import com.silenteight.hsbc.bridge.analysis.dto.FeatureDto;
import com.silenteight.hsbc.bridge.analysis.event.CreateAnalysisEvent;
import com.silenteight.hsbc.bridge.model.ModelUseCase;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AnalysisService {

  private final ModelUseCase modelUseCase;
  private final AnalysisServiceApi analysisServiceApi;
  private final ApplicationEventPublisher eventPublisher;

  void createAnalysisWithDataset(String datasetId) {
    var solvingModel = modelUseCase.getSolvingModel();
    var analysisName = createAnalysis(solvingModel);

    addDataset(analysisName, datasetId);

    eventPublisher.publishEvent(CreateAnalysisEvent.builder()
        .analysisName(analysisName)
        .datasetName(datasetId)
        .solvingModelName(solvingModel.getName())
        .build());
  }

  private String createAnalysis(SolvingModelDto solvingModel) {
    var request = CreateAnalysisRequestDto.builder()
        .name(UUID.randomUUID().toString())
        .policy(solvingModel.getPolicyName())
        .strategy(solvingModel.getStrategyName())
        .features(map(solvingModel.getFeatures()))
        .build();

    return analysisServiceApi.createAnalysis(request).getName();
  }

  private static List<FeatureDto> map(List<com.silenteight.hsbc.bridge.model.FeatureDto> features) {
    return features.stream().map(f ->
        FeatureDto.builder()
            .name(f.getName())
            .agentConfig(f.getAgentConfig())
            .build())
        .collect(Collectors.toList());
  }

  private void addDataset(String analysis, String dataset) {
    var request = AddDatasetRequestDto.builder()
        .analysis(analysis)
        .dataset(dataset)
        .build();

    analysisServiceApi.addDataset(request);
  }
}
