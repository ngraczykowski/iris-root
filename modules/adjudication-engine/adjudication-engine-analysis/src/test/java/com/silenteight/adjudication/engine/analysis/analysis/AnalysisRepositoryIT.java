package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class AnalysisRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private AnalysisRepository analysisRepository;

  @Test
  void shouldGetStrategy() {
    var analysis = AnalysisEntity
        .builder()
        .id(1L)
        .strategy("strategies/BACK_TEST")
        .policy("policies/541e8bc6-922c-41a1-aee2-b9e08ea0d504")
        .build();
    var savedEntity = analysisRepository.save(analysis);
    assertThat(analysisRepository.getStrategyById(savedEntity.getId()).getStrategy())
        .isEqualTo("strategies/BACK_TEST");
  }
}
