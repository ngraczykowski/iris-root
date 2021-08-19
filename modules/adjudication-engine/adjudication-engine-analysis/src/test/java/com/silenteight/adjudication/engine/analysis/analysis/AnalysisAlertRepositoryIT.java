package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.createAnalysisAlertEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AnalysisAlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  AnalysisAlertRepository repository;

  @Test
  void shouldSaveAnalysisAlert() {
    var entity = repository.save(createAnalysisAlertEntity(1, 1));
    assertThat(entity.getId().getAlertId()).isEqualTo(1);
  }
}
