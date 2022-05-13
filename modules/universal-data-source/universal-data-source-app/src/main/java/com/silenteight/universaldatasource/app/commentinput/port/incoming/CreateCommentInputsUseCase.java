package com.silenteight.universaldatasource.app.commentinput.port.incoming;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;
import com.silenteight.datasource.comments.api.v2.CommentInput;

import java.util.List;

public interface CreateCommentInputsUseCase {

  BatchCreateCommentInputResponse addCommentInputs(List<CommentInput> commentInputs);

}
