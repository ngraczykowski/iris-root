package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.domain.AlertTemplateModel;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentFacade {

  private final GenerateRecommendationCommentUseCase recommendationCommentUseCase;

  public String generate(String templateName, AlertTemplateModel alertTemplateModel) {
    return recommendationCommentUseCase.generate(templateName, alertTemplateModel);
  }
}
