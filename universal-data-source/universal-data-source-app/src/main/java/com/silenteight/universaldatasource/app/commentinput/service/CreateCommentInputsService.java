package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;
import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.CreateCommentInputsUseCase;
import com.silenteight.universaldatasource.app.commentinput.port.outgoing.CommentInputDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class CreateCommentInputsService implements CreateCommentInputsUseCase {

  private final CommentInputDataAccess dataAccess;

  private final AlertCommentInputMapper alertCommentInputMapper;

  @Timed(value = "uds.comment-input.use_cases", extraTags = { "action", "addCommentInputs" })
  @Override
  public BatchCreateCommentInputResponse addCommentInputs(List<CommentInput> commentInputs) {

    List<AlertCommentInput> alertCommentInputs = alertCommentInputMapper.map(commentInputs);
    var createdCommentInputs = dataAccess.saveAll(alertCommentInputs);

    if (log.isDebugEnabled()) {
      log.debug("Saved comment inputs: count={}", createdCommentInputs.size());
    }

    return BatchCreateCommentInputResponse.newBuilder()
        .addAllCreatedCommentInputs(createdCommentInputs)
        .build();
  }
}
