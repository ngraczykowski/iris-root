package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.sep.base.aspects.metrics.Timed;

@RequiredArgsConstructor
class GenerateMatchCommentsUseCase {

  private final CommentFacade commentFacade;
  private final String templateName;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  String generateComments(AlertContext alertContext) {
    return commentFacade.generateComment(templateName, alertContext);
  }
}
