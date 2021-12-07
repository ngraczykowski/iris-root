package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CommentInputGrpcAdapter implements CommentInputServiceClient {

  public static final String COULD_NOT_CREATE_COMMENT_INPUTS_ERR_MSG =
      "Could not create comment inputs";
  private final CommentInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public BatchCreateCommentInputOut createCommentInputs(BatchCreateCommentInputIn request) {
    return Try.of(
            () -> getStub().batchCreateCommentInput(request.toBatchCreateCommentInputRequest()))
        .map(BatchCreateCommentInputOut::createFrom)
        .onSuccess(response -> log.info("Created comment inputs with result = {}", response))
        .onFailure(e -> log.error(COULD_NOT_CREATE_COMMENT_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_CREATE_COMMENT_INPUTS_ERR_MSG, e));
  }

  private CommentInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
