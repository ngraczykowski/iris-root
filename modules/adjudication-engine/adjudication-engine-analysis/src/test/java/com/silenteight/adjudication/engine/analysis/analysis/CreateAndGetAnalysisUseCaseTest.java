package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;
import com.silenteight.adjudication.engine.common.grpc.InvalidAnalysisException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class CreateAndGetAnalysisUseCaseTest {

  private final CreateAndGetAnalysisUseCase useCase = AnalysisFixtures.inMemoryAnalysisUseCase();

  @Test
  @Disabled
  void createAnalysis() {
    var analysis = AnalysisFixtures.randomAnalysis();

    var created = useCase.createAndGetAnalysis(analysis);

    assertAnalysisEqualsIgnoringStateAndName(analysis, created);
  }

  @Test
  void createAnalysisWithoutLabelsCategoriesAndFeatures() {
    var analysis = AnalysisFixtures.randomAnalysisWithoutLabelsCategoriesAndFeatures();

    assertThrows(
        InvalidAnalysisException.class,
        () -> useCase.createAndGetAnalysis(analysis));
  }

  @Test
  void createMultipleAnalysis() {
    for (var analysis : AnalysisFixtures.randomAnalysisList()) {
      var created = useCase.createAndGetAnalysis(analysis);
      assertAnalysisEqualsIgnoringStateAndName(analysis, created);
    }
  }

  private static void assertAnalysisEqualsIgnoringStateAndName(
      Analysis analysis, Analysis created) {
    assertThat(created.getPolicy()).isEqualTo(analysis.getPolicy());
    assertThat(created.getStrategy()).isEqualTo(analysis.getStrategy());
    assertThat(created.getCategoriesCount()).isEqualTo(analysis.getCategoriesCount());
    assertThat(created.getFeaturesCount()).isEqualTo(analysis.getFeaturesCount());
    assertThat(created.getLabelsCount()).isEqualTo(analysis.getLabelsCount());
    assertThat(created.getNotificationFlags()).isEqualTo(analysis.getNotificationFlags());

    assertThat(created).satisfies(a -> {
      assertThat(a.getCreateTime()).isNotNull();
      assertThat(a.getName()).isNotBlank();
      assertThat(a.getState()).isEqualTo(State.NEW);
    });
  }
}
