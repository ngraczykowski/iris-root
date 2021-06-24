package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GenerateCommentsUseCase {

  private final CommentFacade commentFacade;

  GenerateCommentsResponse generateComments(GenerateCommentsRequest request) {
    var comment = commentFacade.generateComment("", request.getAlertContext());
    return new GenerateCommentsResponse();
  }
}
