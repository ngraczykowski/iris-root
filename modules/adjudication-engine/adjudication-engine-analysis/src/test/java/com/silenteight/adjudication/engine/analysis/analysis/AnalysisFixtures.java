package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;

import java.util.*;

import static java.util.concurrent.ThreadLocalRandom.current;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalysisFixtures {

  private static final String[] STRATEGIES = { "back_test", "standard" };
  private static final String[] CATEGORIES = {
      "source_system", "country", "customer_type", "hit_category" };

  static CreateAndGetAnalysisUseCase inMemoryAnalysisUseCase() {
    var repository = new InMemoryAnalysisRepository();
    var createAnalysisUseCase = new CreateAnalysisUseCase(
        repository.getRepository(), new FakeCategoryProvider(), new FakeFeatureProvider());
    var getAnalysisUseCase = new GetAnalysisUseCase(repository.getQueryRepository());

    return new CreateAndGetAnalysisUseCase(createAnalysisUseCase, getAnalysisUseCase);
  }

  public static Analysis randomAnalysis() {
    var builder = Analysis.newBuilder();
    var analysisEntity = randomAnalysisEntity();
    analysisEntity.updateBuilder(builder);

    builder.addAllCategories(getRandomCategories(analysisEntity.getCategories().size()));
    builder.addAllFeatures(getRandomFeatures(analysisEntity.getFeatures().size()));

    return builder.build();
  }

  static Analysis randomAnalysisWithoutLabelsCategoriesAndFeatures() {
    return randomAnalysisEntityWithoutLabelsCategoriesAndFeatures()
        .updateBuilder(Analysis.newBuilder()).build();
  }

  static List<Analysis> randomAnalysisList() {
    List<Analysis> analysisList = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      analysisList.add(randomAnalysisEntity().updateBuilder(Analysis.newBuilder()).build());
    }
    return analysisList;
  }

  static AnalysisEntity randomAnalysisEntity() {
    return AnalysisEntity.builder()
        .state(getRandomAnalysisState())
        .strategy("strategies/" + STRATEGIES[current().nextInt(0, STRATEGIES.length)])
        .policy("policies/" + UUID.randomUUID())
        .labels(getRandomLabels())
        .categories(getRandomAnalysisCategories())
        .features(getRandomAnalysisFeatures())
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

  static List<AnalysisCategory> getRandomAnalysisCategories() {
    long id = current().nextLong(100, 1000);
    List<AnalysisCategory> categories = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      categories.add(AnalysisCategory.builder().categoryId(id++).build());
    }
    return categories;
  }

  static List<String> getRandomCategories(int count) {
    List<String> categories = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      String category = CATEGORIES[current().nextInt(0, CATEGORIES.length)];
      categories.add("categories/" + category);
    }
    return categories;
  }

  static List<AnalysisFeature> getRandomAnalysisFeatures() {
    long id = current().nextLong(100, 1000);
    List<AnalysisFeature> features = new ArrayList<>();
    for (int i = 0; i < current().nextInt(10, 100); i++) {
      features.add(AnalysisFeature.builder().agentConfigFeatureId(id++).build());
    }
    return features;
  }

  static List<Feature> getRandomFeatures(int count) {
    List<Feature> features = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      features.add(Feature.newBuilder()
          .setFeature("features/" + i)
          .setAgentConfig("agents/" + i + "/versions/1.2.3/configs/1")
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
