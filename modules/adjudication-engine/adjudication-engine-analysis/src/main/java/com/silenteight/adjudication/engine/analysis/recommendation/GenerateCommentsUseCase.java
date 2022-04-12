package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.aspects.metrics.Timed;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class GenerateCommentsUseCase {

  private final CommentFacade commentFacade;
  private final String templateName;
  private final String matchTemplateName;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  GenerateCommentsResponse generateComments(GenerateCommentsRequest request) {
    var comment = commentFacade.generateComment(templateName, request.getAlertContext());
    return new GenerateCommentsResponse(comment);
  }

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  Map<String, String> generateMatchComments(List<MatchContext> matches) {
    return commentFacade.generateMatchComments(matchTemplateName, matches);
  }
}
