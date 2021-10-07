package com.silenteight.adjudication.engine.analysis.commentinput;

import com.silenteight.adjudication.engine.analysis.commentinput.domain.MissingCommentInputsResult;

import java.util.Optional;

public interface CommentInputDataAccess {

  Optional<MissingCommentInputsResult> getMissingCommentInputs(long analysisId);
}
