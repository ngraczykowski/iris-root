package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.commentinput.domain.MissingCommentInputsResult;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
class JdbcCommentInputDataAccess implements CommentInputDataAccess {

  private final SelectMissingAlertCommentInputQuery selectMissingAlertCommentInputQuery;

  @Override
  @Transactional(readOnly = true)
  public Optional<MissingCommentInputsResult> getMissingCommentInputs(long analysisId) {
    return selectMissingAlertCommentInputQuery.execute(analysisId);
  }
}
