package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Analysis.State;
import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = {
    "classpath:fixtures/01_Alerts.sql",
    "classpath:fixtures/02_Dataset.sql",
    "classpath:fixtures/03_Analysis.sql",
})
@SqlMergeMode(MergeMode.MERGE)
class AnalysisQueryRepositoryIT extends BaseDataJpaTest {

  @Autowired
  AnalysisQueryRepository repository;

  @Test
  void givenNoDatasets_shouldReturnAnalysisAsNew() {
    assertThatAnalysisStateIs(State.NEW);
  }

  @Test
  @Sql
  void givenDatasetWithNoAlerts_shouldReturnAnalysisAsNew() {
    assertThatAnalysisStateIs(State.NEW);
  }

  @Test
  @Sql
  void givenDatasetWithAlerts_shouldReturnAnalysisAsPlanning() {
    assertThatAnalysisStateIs(State.PLANNING);
  }

  @Test
  @Sql
  void givenDatasetWithSolvedAlerts_shouldReturnAnalysisAsDone() {
    assertThatAnalysisStateIs(State.DONE);
  }

  @Test
  @Sql
  void givenDatasetWithSingleUnsolvedAlert_shouldReturnAnalysisAsRunning() {
    assertThatAnalysisStateIs(State.RUNNING);
  }

  private void assertThatAnalysisStateIs(State expectedState) {
    assertThat(repository.findById(1))
        .isNotEmpty()
        .hasValueSatisfying(query ->
            assertThat(query.toAnalysis().getState()).isEqualTo(expectedState));
  }
}
