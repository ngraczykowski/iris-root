package com.silenteight.adjudication.engine.analysis.matchsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetMatchSolutionUseCaseTest {

  private GetMatchSolutionUseCase getMatchSolutionUseCase;

  @BeforeEach
  void setUp() {
    getMatchSolutionUseCase = new GetMatchSolutionUseCase(new InMemoryMatchSolutionDataAccess());
  }

  @Test
  void shouldGetMatchSolution() {
    assertThat(getMatchSolutionUseCase.getMatchSolution("analysis/1/match-solutions/1"))
        .isNotNull();
  }
}
