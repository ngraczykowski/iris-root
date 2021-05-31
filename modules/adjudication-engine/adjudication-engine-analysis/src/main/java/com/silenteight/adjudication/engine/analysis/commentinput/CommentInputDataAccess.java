package com.silenteight.adjudication.engine.analysis.commentinput;

import java.util.Optional;

public interface CommentInputDataAccess {

  Optional<MissingCommentInputsResult> getMissingCommentInputs(long analysisId);
}
