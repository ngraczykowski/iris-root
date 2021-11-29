package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.DeleteCommentInputsUseCase;
import com.silenteight.universaldatasource.app.commentinput.port.outgoing.CommentInputDataAccess;

import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
class DeleteCommentInputsService implements DeleteCommentInputsUseCase {

  private final CommentInputDataAccess dataAccess;

  @Timed(value = "uds.comment-input.use_cases", extraTags = { "action", "deleteCommentInputs" })
  @Override
  public void delete(List<String> alerts) {

    log.info("Deleting comment inputs: alertCount={}, alerts={}", alerts.size(), alerts);

    int deletedCount = dataAccess.delete(alerts);

    log.info("Comment inputs removed, count={}", deletedCount);
  }
}
