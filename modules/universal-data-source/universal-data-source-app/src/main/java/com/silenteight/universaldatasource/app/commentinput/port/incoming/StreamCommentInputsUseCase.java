package com.silenteight.universaldatasource.app.commentinput.port.incoming;

import com.silenteight.datasource.comments.api.v2.CommentInput;

import java.util.List;
import java.util.function.Consumer;

public interface StreamCommentInputsUseCase {

  void streamCommentInput(List<String> alertsList, Consumer<CommentInput> consumer);
}
