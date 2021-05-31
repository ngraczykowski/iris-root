package com.silenteight.adjudication.engine.comments.commentinput;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlertCommentInputFacade {

  @NonNull
  private final CreateAlertCommentInputsUseCase createAlertCommentInputsUseCase;

  public void createAlertCommentInputs(Iterable<CommentInput> commentInputs) {
    createAlertCommentInputsUseCase.createAlertCommentInputs(commentInputs);
  }
}
