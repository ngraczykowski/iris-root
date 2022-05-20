package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.sep.base.aspects.metrics.Timed;

@RequiredArgsConstructor
class GenerateMatchCommentsUseCase {

  private final CommentFacade commentFacade;
  private final String templateName;
  private final String matchTemplateName;

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "recommendation" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  String generateComments(AlertContext alertContext) {
    return commentFacade
        .generateComment(templateName, alertContext);
  }
}
