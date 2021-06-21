package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentFacade {

  private final GenerateCommentUseCase generateCommentUseCase;

  public String generateComment(String templateName, AlertContext alertModel) {
    return generateCommentUseCase.generateComment(templateName, alertModel);
  }
}
