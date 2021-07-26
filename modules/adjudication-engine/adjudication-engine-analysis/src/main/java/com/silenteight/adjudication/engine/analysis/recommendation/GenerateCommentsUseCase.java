package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.sep.base.aspects.metrics.Timed;

@RequiredArgsConstructor
class GenerateCommentsUseCase {

  private final CommentFacade commentFacade;
  private final String templateName;

  @Timed("ae.analysis.use_case.recommendation.generate_comments")
  GenerateCommentsResponse generateComments(GenerateCommentsRequest request) {
    var comment = commentFacade.generateComment(templateName, request.getAlertContext());
    return new GenerateCommentsResponse(comment);
  }
}
