package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.commentinput.domain.MissingCommentInputsResult;

import java.util.Map;
import java.util.Optional;

class InMemoryCommentInputDataAccess implements CommentInputDataAccess {

  @Override
  public Optional<MissingCommentInputsResult> getMissingCommentInputs(long analysisId) {
    return Optional.empty();
  }

  @Override
  public Optional<Map<String, Object>> getCommentInputByAlertId(long alertId) {
    return Optional.empty();
  }
}
