package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import com.silenteight.sep.base.aspects.metrics.Timed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.commentinput.domain.MissingCommentInputsResult;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
class JdbcCommentInputDataAccess implements CommentInputDataAccess {

  private final SelectMissingAlertCommentInputQuery selectMissingAlertCommentInputQuery;
  private final SelectCommentInputByAlertIdQuery selectCommentInputByAlertIdQuery;

  @Override
  @Transactional(readOnly = true)
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public Optional<MissingCommentInputsResult> getMissingCommentInputs(long analysisId) {
    return selectMissingAlertCommentInputQuery.execute(analysisId);
  }

  @Override
  @Transactional(readOnly = true)
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public Optional<Map<String, Object>> getCommentInputByAlertId(long alertId) {
    return selectCommentInputByAlertIdQuery.execute(alertId);
  }
}
