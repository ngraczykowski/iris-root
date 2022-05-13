package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsResponse;
import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.BatchGetAlertsCommentInputsUseCase;
import com.silenteight.universaldatasource.app.commentinput.port.outgoing.CommentInputDataAccess;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
class BatchGetAlertsCommentInputsService implements BatchGetAlertsCommentInputsUseCase {

  private final CommentInputDataAccess dataAccess;

  private final CommentInputMapper commentInputMapper;

  @Timed(
      value = "uds.comment-input.use_cases",
      extraTags = { "action", "batchGetCommentInputs" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  @Override
  public BatchGetAlertsCommentInputsResponse batchGetAlertsCommentInputs(List<String> alerts) {

    if (log.isDebugEnabled()) {
      log.debug("Getting comment inputs: count={}", alerts.size());
    }

    var commentInputs = getCommentInputs(dataAccess.batchGetCommentInputs(alerts));

    if (log.isDebugEnabled()) {
      log.debug("Returning comment inputs : count={}", commentInputs.size());
    }

    return BatchGetAlertsCommentInputsResponse.newBuilder()
        .addAllCommentInputs(commentInputs)
        .build();
  }

  private List<CommentInput> getCommentInputs(List<AlertCommentInput> alertCommentInputs) {
    return alertCommentInputs.stream()
        .map(commentInputMapper::map)
        .collect(Collectors.toList());
  }
}
