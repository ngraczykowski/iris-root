package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.StreamCommentInputsUseCase;

import java.util.function.Consumer;

@RequiredArgsConstructor
class CommentInputAdapter {

  private final StreamCommentInputsUseCase streamCommentInputsUseCase;

  private final CommentInputVersionMapper versionMapper;

  void streamCommentInputs(StreamCommentInputsRequest request, Consumer<CommentInput> onNext) {
    streamCommentInputsUseCase.streamCommentInput(
        request.getAlertsList(),
        commentInput -> onNext.accept(versionMapper.map(commentInput)));
  }
}
