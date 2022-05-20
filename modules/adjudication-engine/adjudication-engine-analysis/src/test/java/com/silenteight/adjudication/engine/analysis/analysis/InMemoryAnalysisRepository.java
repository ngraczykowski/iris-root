package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.StrategyName;
import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

class InMemoryAnalysisRepository extends BasicInMemoryRepository<AnalysisEntity>
    implements AnalysisRepository {

  AnalysisRepository getRepository() {
    return this;
  }

  AnalysisQueryRepository getQueryRepository() {
    return id -> InMemoryAnalysisRepository.this
        .findById(id)
        .map(a -> AnalysisQuery.builder()
            .analysis(a)
            .alertCount(0L)
            .pendingAlerts(0L)
            .datasetCount(0L)
            .categoryQueries(getCategoriesQuery(a))
            .featureQueries(getFeaturesQuery(a))
            .build());
  }

  private static List<AnalysisFeatureQuery> getFeaturesQuery(AnalysisEntity analysisEntity) {
    return analysisEntity.getFeatures()
        .stream()
        .map(feature -> AnalysisFeatureQuery
            .builder()
            .id(feature.getId())
            .agentConfig("agentConfig")
            .feature("feature")
            .build())
        .collect(Collectors.toList());
  }

  private static List<AnalysisCategoryQuery> getCategoriesQuery(AnalysisEntity analysisEntity) {
    return analysisEntity.getCategories()
        .stream()
        .map(category -> AnalysisCategoryQuery
            .builder()
            .categoryId(category.getCategoryId())
            .name("categoryName")
            .build())
        .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public AnalysisEntity save(
      AnalysisEntity analysisEntity) {
    return super.save(analysisEntity);
  }

  @Override
  public StrategyName getStrategyById(long analysisId) {
    return null;
  }
}
