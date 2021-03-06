package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFixture.createMatchSolutionCollection;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateMatchSolutionUseCaseTest {

  private InMemoryMatchSolutionDataAccess matchSolutionDataAccess;
  private CreateMatchSolutionsUseCase createMatchSolutionsUseCase;
  @Mock
  private ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter;

  @BeforeEach
  void setUp() {
    matchSolutionDataAccess = new InMemoryMatchSolutionDataAccess();
    createMatchSolutionsUseCase =
        new CreateMatchSolutionsUseCase(matchSolutionDataAccess, protoMessageToObjectNodeConverter);
  }

  @Test
  void shouldSaveAllSolutions() {
    createMatchSolutionsUseCase.createMatchSolutions(createMatchSolutionCollection(10));
    assertThat(matchSolutionDataAccess.count()).isEqualTo(10);
  }
}
