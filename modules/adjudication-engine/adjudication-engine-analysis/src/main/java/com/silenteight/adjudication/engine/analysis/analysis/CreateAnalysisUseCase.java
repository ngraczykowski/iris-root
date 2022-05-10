package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.common.grpc.InvalidAnalysisException;
import com.silenteight.sep.base.aspects.metrics.Timed;

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
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  String createAnalysis(Analysis analysis) {
    validateAnalysis(analysis);
    return repository.save(createEntity(analysis)).getName();
  }

  private static void validateAnalysis(Analysis analysis) {
    if (areFeaturesAndCategoriesEmpty(analysis)) {
      throw new InvalidAnalysisException("Analysis doesn't have any categories nor features");
    }
  }

  private static boolean areFeaturesAndCategoriesEmpty(Analysis analysis) {
    return analysis.getCategoriesList().isEmpty() && analysis.getFeaturesList().isEmpty();
  }

  private AnalysisEntity createEntity(Analysis analysis) {
    var builder = AnalysisEntity.builder();
    builder.policy(analysis.getPolicy());
    builder.strategy(analysis.getStrategy());

    var notificationFlags = analysis.getNotificationFlags();

    builder.attachMetadata(notificationFlags.getAttachMetadata());

    builder.attachRecommendation(notificationFlags.getAttachRecommendation());

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
