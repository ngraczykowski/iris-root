package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.dto.AddDatasetRequestDto;
import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;
import com.silenteight.hsbc.bridge.analysis.dto.CreateAnalysisRequestDto;
import com.silenteight.hsbc.bridge.analysis.dto.FeatureDto;
import com.silenteight.hsbc.bridge.model.ModelUseCase;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AnalysisFacade {

  private final AnalysisRepository analysisRepository;
  private final AnalysisServiceApi analysisServiceApi;
  private final ModelUseCase modelUseCase;

  @Transactional
  public AnalysisDto createAnalysisWithDataset(String dataset) {
    var analysis = createAnalysis();
    addDatasetToAnalysis(analysis.getName(), dataset);

    var entity = new AnalysisEntity(analysis, dataset);
    analysisRepository.save(entity);

    log.info("Analysis: {} created", entity);
    return entity.toAnalysisDto();
  }

  public AnalysisDto getById(long id) {
    return analysisRepository.findById(id)
        .map(AnalysisEntity::toAnalysisDto)
        .orElseThrow(() -> new AnalysisNotFoundException(id));
  }

  private AnalysisDto createAnalysis() {
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

  private void addDatasetToAnalysis(String analysis, String dataset) {
    var request = AddDatasetRequestDto.builder()
        .analysis(analysis)
        .dataset(dataset)
        .build();

    analysisServiceApi.addDataset(request);
  }
}
