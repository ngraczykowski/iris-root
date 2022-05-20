package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.randomAnalysisEntities;
import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.randomAnalysisEntity;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AnalysisFeatureVectorElementsConfigurationIT extends BaseDataJpaTest {

  @Autowired private AnalysisRepository repository;

  @Test
  void persistAndFindWithEntityManagerReturnsSameAnalysisEntity() {
    var analysisEntity = randomAnalysisEntity();

    analysisEntity = entityManager.persistAndFlush(analysisEntity);
    entityManager.clear();

    compareAnalysisEntityInDatabase(analysisEntity);
  }

  @Test
  void savesAnalysisEntityToRepository() {
    var analysisEntity = randomAnalysisEntity();

    analysisEntity = repository.save(analysisEntity);
    entityManager.flush();
    entityManager.clear();

    compareAnalysisEntityInDatabase(analysisEntity);
  }

  @Test
  void savesMultipleAnalysisEntityToRepository() {
    for (var analysisEntity : randomAnalysisEntities()) {
      analysisEntity = repository.save(analysisEntity);
      entityManager.flush();
      entityManager.clear();

      compareAnalysisEntityInDatabase(analysisEntity);
    }
  }

  private void compareAnalysisEntityInDatabase(AnalysisEntity analysisEntity) {
    var foundAnalysisEntity = entityManager.find(AnalysisEntity.class, analysisEntity.getId());

    assertThat(foundAnalysisEntity)
        .usingRecursiveComparison()
        .ignoringFields("id", "alertedAt", "createdAt")
        .isEqualTo(analysisEntity);
  }
}
