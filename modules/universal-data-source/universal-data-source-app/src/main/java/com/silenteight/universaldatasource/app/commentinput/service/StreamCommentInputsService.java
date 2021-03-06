package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.sep.base.aspects.metrics.Timed;
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

  @Timed(
      value = "uds.comment-input.use_cases",
      extraTags = { "action", "streamCommentInput" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  @Override
  public void streamCommentInput(List<String> alerts, Consumer<CommentInput> consumer) {

    if (log.isDebugEnabled()) {
      log.debug("Streaming comment inputs: count={}", alerts.size());
    }

    var commentInputsCount = dataAccess.stream(
        alerts,
        commentInput -> consumer.accept(commentInputMapper.map(commentInput)));

    if (log.isDebugEnabled()) {
      log.debug("Finished streaming comment inputs: count={}", commentInputsCount);
    }
  }
}
