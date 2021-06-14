package com.silenteight.adjudication.engine.analysis.commentinput;

import com.silenteight.adjudication.engine.comments.commentinput.AlertCommentInputFacade;
import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.com.google.common.collect.Iterables.elementsEqual;

@ExtendWith(MockitoExtension.class)
class FetchAllMissingCommentInputsUseCaseTest {

  private FetchAllMissingCommentInputsUseCase useCase;
  @Mock
  private AlertCommentInputFacade alertCommentInputFacade;
  @Mock
  private CommentInputDataAccess commentInputDataAccess;
  @Mock
  private CommentInputServiceClient commentInputClient;

  @BeforeEach
  void setUp() {
    useCase = new FetchAllMissingCommentInputsUseCase(
        commentInputDataAccess, commentInputClient, alertCommentInputFacade);
  }

  @Test
  void shouldHandleRequest() {
    var alertName = "alerts/1";
    var expectedCommentInput = createCommentInputList(alertName);

    when(commentInputClient.getCommentInputs(any(StreamCommentInputsRequest.class)))
        .thenReturn(expectedCommentInput);
    when(commentInputDataAccess.getMissingCommentInputs(1))
        .thenReturn(Optional.of(new MissingCommentInputsResult(List.of(alertName))))
        .thenReturn(Optional.of(new MissingCommentInputsResult(List.of())));

    useCase.fetchAllMissingCommentInputsValues("analysis/1");

    verify(alertCommentInputFacade).createAlertCommentInputs(
        argThat(list -> elementsEqual(list, expectedCommentInput)));
  }

  @NotNull
  private List<CommentInput> createCommentInputList(String alert) {
    var commentInput = CommentInput.newBuilder()
        .setAlert(alert)
        .setAlertCommentInput(Struct.newBuilder()
            .putFields("test", Value.newBuilder().setStringValue("Test").build())
            .build())
        .build();
    return List.of(commentInput);
  }
}
