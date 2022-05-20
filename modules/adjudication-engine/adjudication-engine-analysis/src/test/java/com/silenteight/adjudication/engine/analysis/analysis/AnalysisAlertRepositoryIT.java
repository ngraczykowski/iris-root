package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisFixtures.createAnalysisAlertEntity;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AnalysisAlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  AnalysisAlertRepository repository;

  @Test
  void shouldSaveAnalysisAlert() {
    var entities = repository.saveAll(List.of(createAnalysisAlertEntity(1, 1)));
    assertThat(entities)
        .first()
        .satisfies(r -> assertThat(r.getName()).isEqualTo("analysis/1/alerts/1"));
  }
}
