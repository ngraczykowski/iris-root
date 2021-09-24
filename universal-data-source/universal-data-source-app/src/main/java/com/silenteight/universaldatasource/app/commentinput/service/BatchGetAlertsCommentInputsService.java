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

  @Timed(value = "uds.comment-input.use_cases", extraTags = { "action", "batchGetCommentInputs" })
  @Override
  public BatchGetAlertsCommentInputsResponse batchGetAlertsCommentInputs(List<String> alerts) {
    log.debug("Getting comment inputs : commentInputsCount={}", alerts.size());

    var commentInputs = getCommentInputs(dataAccess.batchGetCommentInputs(alerts));
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
