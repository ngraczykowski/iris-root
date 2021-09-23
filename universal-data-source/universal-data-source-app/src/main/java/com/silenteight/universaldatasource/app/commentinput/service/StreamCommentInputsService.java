package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.StreamCommentInputsUseCase;
import com.silenteight.universaldatasource.app.commentinput.port.outgoing.CommentInputDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
class StreamCommentInputsService implements StreamCommentInputsUseCase {

  private final CommentInputDataAccess dataAccess;

  private final CommentInputMapper commentInputMapper;

  @Override
  public void streamCommentInput(List<String> alerts, Consumer<CommentInput> consumer) {
    log.debug("Streaming comment inputs");

    var commentInputsCount = dataAccess.stream(
        alerts,
        commentInput -> consumer.accept(commentInputMapper.map(commentInput)));

    log.info("Finished streaming comment inputs: commentInputs={}", commentInputsCount);
  }
}
