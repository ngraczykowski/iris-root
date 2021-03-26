package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.Analysis.State;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class CreateAnalysisUseCase {

  @NonNull
  private final AnalysisRepository repository;

  @Transactional
  Analysis createAnalysis(Analysis analysis) {
    return repository.save(createEntity(analysis)).toAnalysis();
  }

  private AnalysisEntity createEntity(Analysis analysis) {
    var builder = AnalysisEntity.builder();
    builder.policy(analysis.getPolicy());
    builder.strategy(analysis.getStrategy());
    builder.state(State.NEW);

    if (analysis.getLabelsCount() > 0) {
      builder.labels(analysis.getLabelsMap());
    }

    if (analysis.getCategoriesCount() > 0) {
      builder.categories(createCategories(analysis.getCategoriesList()));
    }

    if (analysis.getFeaturesCount() > 0) {
      builder.features(createFeatures(analysis.getFeaturesList()));
    }

    return builder.build();
  }

  private List<AnalysisCategoryEntity> createCategories(List<String> categories) {
    return categories
        .stream()
        .map(c -> AnalysisCategoryEntity
            .builder()
            .category(c)
            .build())
        .collect(Collectors.toList());
  }

  private List<AnalysisFeatureEntity> createFeatures(List<Feature> features) {
    return features
        .stream()
        .map(f -> AnalysisFeatureEntity
            .builder()
            .agentConfig(f.getAgentConfig())
            .feature(f.getFeature())
            .build())
        .collect(Collectors.toList());
  }
}
