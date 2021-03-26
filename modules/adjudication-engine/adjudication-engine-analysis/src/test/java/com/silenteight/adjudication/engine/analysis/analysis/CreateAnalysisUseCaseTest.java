package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class CreateAnalysisUseCaseTest {

  private static final String[] IGNORED_PROTO_FIELDS =
      { "createTime_", "name_", "state_", "memoizedHashCode" };
  private AnalysisFacade facade = AnalysisFixtures.inMemoryAnalysisFacade();

  @Test
  void createAnalysis() {
    var analysis = singletonList(AnalysisFixtures.randomAnalysis());

    var created = facade.createAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  @Test
  void createAnalysisWithoutLabelsCategoriesAndFeatures() {
    var analysis = singletonList(
        AnalysisFixtures.randomAnalysisWithoutLabelsCategoriesAndFeatures());

    var created = facade.createAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  @Test
  void createMultipleAnalysis() {
    var analysis = AnalysisFixtures.randomAnalysisList();

    var created = facade.createAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  private void assertAnalysisEqualsIgnoringStateAndName(
      List<Analysis> analysis, List<Analysis> created) {
    assertThat(created)
        .usingRecursiveComparison()
        .ignoringFields(IGNORED_PROTO_FIELDS)
        .isEqualTo(analysis);

    assertThat(created).allSatisfy(a -> {
      assertThat(a.getCreateTime()).isNotNull();
      assertThat(a.getName()).isNotBlank();
      assertThat(a.getState()).isEqualTo(State.NEW);
    });
  }
}
