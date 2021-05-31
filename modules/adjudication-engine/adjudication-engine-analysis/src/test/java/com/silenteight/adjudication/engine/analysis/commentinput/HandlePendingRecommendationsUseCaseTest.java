package com.silenteight.adjudication.engine.analysis.commentinput;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlePendingRecommendationsUseCaseTest {

  @Mock
  FetchAllMissingCommentInputsUseCase fetchAllMissingCommentInputsUseCase;

  HandlePendingRecommendationsUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new HandlePendingRecommendationsUseCase(fetchAllMissingCommentInputsUseCase);
  }

  @Test
  void shouldCallIncludedUseCase() {
    var request = PendingRecommendations.newBuilder()
        .addAllAnalysis(List.of("analysis/1", "analysis/2"))
        .build();

    underTest.handlePendingRecommendations(request);

    verify(fetchAllMissingCommentInputsUseCase).fetchAllMissingCommentInputsValues("analysis/1");
    verify(fetchAllMissingCommentInputsUseCase).fetchAllMissingCommentInputsValues("analysis/2");
  }
}
