package com.silenteight.adjudication.engine.analysis.matchsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFixture.createMatchSolutionCollection;
import static org.assertj.core.api.Assertions.*;

class CreateMatchSolutionUseCaseTest {

  private MatchSolutionRepository matchSolutionRepository;
  private CreateMatchSolutionsUseCase createMatchSolutionsUseCase;

  @BeforeEach
  void setUp() {
    matchSolutionRepository = new InMemoryMatchSolutionRepository();
    createMatchSolutionsUseCase = new CreateMatchSolutionsUseCase(matchSolutionRepository);
  }

  @Test
  void shouldSaveAllSolutions() {
    createMatchSolutionsUseCase.createMatchSolutions(createMatchSolutionCollection(10));
    assertThat(matchSolutionRepository.count()).isEqualTo(10);
  }
}
