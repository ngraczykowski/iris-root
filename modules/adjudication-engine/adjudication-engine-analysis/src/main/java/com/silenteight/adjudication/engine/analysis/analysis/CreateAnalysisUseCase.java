package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
class CreateAnalysisUseCase {

  @NonNull
  private final AnalysisRepository repository;

  @NonNull
  private final CategoryProvider categoryProvider;

  @NonNull
  private final FeatureProvider featureProvider;

  @Transactional
  String createAnalysis(Analysis analysis) {
    return repository.save(createEntity(analysis)).getName();
  }

  private AnalysisEntity createEntity(Analysis analysis) {
    var builder = AnalysisEntity.builder();
    builder.policy(analysis.getPolicy());
    builder.strategy(analysis.getStrategy());

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

  private List<AnalysisCategory> createCategories(List<String> categories) {
    return categoryProvider
        .getCategories(categories)
        .stream()
        .map(c -> AnalysisCategory
            .builder()
            .categoryId(c.getId())
            .build())
        .collect(toList());
  }

  private List<AnalysisFeature> createFeatures(List<Feature> features) {
    return featureProvider
        .getFeatures(features)
        .stream()
        .map(f -> AnalysisFeature
            .builder()
            .agentConfigFeatureId(f.getId())
            .build())
        .collect(toList());
  }
}
