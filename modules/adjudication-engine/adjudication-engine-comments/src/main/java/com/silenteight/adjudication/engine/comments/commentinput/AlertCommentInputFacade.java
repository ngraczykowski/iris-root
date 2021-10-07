package com.silenteight.adjudication.engine.comments.commentinput;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlertCommentInputFacade {

  @NonNull
  private final CreateAlertCommentInputsUseCase createAlertCommentInputsUseCase;

  public void createAlertCommentInputs(Iterable<CommentInputResponse> commentInputs) {
    createAlertCommentInputsUseCase.createAlertCommentInputs(commentInputs);
  }
}
