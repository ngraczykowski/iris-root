package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;

import java.util.*;

import static java.util.concurrent.ThreadLocalRandom.current;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AnalysisFixtures {

  private static final String[] STRATEGIES = { "back_test", "standard" };
  private static final String[] CATEGORIES = {
      "source_system", "country", "customer_type", "hit_category" };

  static AnalysisFacade inMemoryAnalysisFacade() {
    var analysisRepository = new InMemoryAnalysisRepository();

    var createAnalysisUseCase = new CreateAnalysisUseCase(analysisRepository);

    return new AnalysisFacade(createAnalysisUseCase);
  }

  static Analysis randomAnalysis() {
    return randomAnalysisEntity().toAnalysis();
  }

  static Analysis randomAnalysisWithoutLabelsCategoriesAndFeatures() {
    return randomAnalysisEntityWithoutLabelsCategoriesAndFeatures().toAnalysis();
  }

  static List<Analysis> randomAnalysisList() {
    List<Analysis> analysisList = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      analysisList.add(randomAnalysisEntity().toAnalysis());
    }
    return analysisList;
  }

  static AnalysisEntity randomAnalysisEntity() {
    return AnalysisEntity.builder()
        .state(getRandomAnalysisState())
        .strategy("strategies/" + STRATEGIES[current().nextInt(0, STRATEGIES.length)])
        .policy("policies/" + UUID.randomUUID())
        .labels(getRandomLabels())
        .categories(getRandomCategories())
        .features(getRandomFeatures())
        .build();
  }

  static AnalysisEntity randomAnalysisEntityWithoutLabelsCategoriesAndFeatures() {
    return AnalysisEntity.builder()
        .state(getRandomAnalysisState())
        .strategy("strategies/" + STRATEGIES[current().nextInt(0, STRATEGIES.length)])
        .policy("policies/" + UUID.randomUUID())
        .build();
  }

  static List<AnalysisEntity> randomAnalysisEntities() {
    List<AnalysisEntity> analysisEntities = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      analysisEntities.add(randomAnalysisEntity());
    }
    return analysisEntities;
  }

  static Map<String, String> getRandomLabels() {
    Map<String, String> labels = new HashMap<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      labels.put("label" + i, "value" + i);
    }
    return labels;
  }

  static List<AnalysisCategoryEntity> getRandomCategories() {
    List<AnalysisCategoryEntity> categories = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      String category = CATEGORIES[current().nextInt(0, CATEGORIES.length)];
      categories.add(AnalysisCategoryEntity.builder().category("categories/" + category).build());
    }
    return categories;
  }

  static List<AnalysisFeatureEntity> getRandomFeatures() {
    List<AnalysisFeatureEntity> features = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      features.add(AnalysisFeatureEntity.builder()
          .feature("feature" + i)
          .agentConfig("agent" + i)
          .build());
    }
    return features;
  }

  static Analysis.State getRandomAnalysisState() {
    // subtract 1 to exclude UNSPECIFIED
    int x = current().nextInt(Analysis.State.class.getEnumConstants().length - 1);
    return Analysis.State.class.getEnumConstants()[x];
  }
}
