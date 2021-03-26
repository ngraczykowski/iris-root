package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CreateAnalysisUseCaseTest {

  private static final String[] IGNORED_PROTO_FIELDS =
      { "createTime_", "name_", "state_", "memoizedHashCode" };
  private AnalysisFacade facade = AnalysisFixtures.inMemoryAnalysisFacade();

  @Test
  void createAnalysis() {
    var analysis = AnalysisFixtures.randomAnalysis();

    var created = facade.createAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  @Test
  void createAnalysisWithoutLabelsCategoriesAndFeatures() {
    var analysis = AnalysisFixtures.randomAnalysisWithoutLabelsCategoriesAndFeatures();

    var created = facade.createAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  @Test
  void createMultipleAnalysis() {
    for (var analysis : AnalysisFixtures.randomAnalysisList()) {
      var created = facade.createAnalysis(analysis);

      assertAnalysisEqualsIgnoringStateAndName(analysis, created);
    }
  }

  private void assertAnalysisEqualsIgnoringStateAndName(
      Analysis analysis, Analysis created) {
    assertThat(created)
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysis);

    assertThat(created).satisfies(a -> {
      assertThat(a.getCreateTime()).isNotNull();
      assertThat(a.getName()).isNotBlank();
      assertThat(a.getState()).isEqualTo(State.NEW);
    });
  }
}
