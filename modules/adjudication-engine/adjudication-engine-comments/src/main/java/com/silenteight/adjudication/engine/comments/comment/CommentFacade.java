package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentFacade {

  private final GenerateAlertCommentUseCase generateCommentUseCase;
  private final GenerateMatchCommentUseCase generateMatchCommentUseCase;

  public String generateComment(String templateName, AlertContext alertModel) {
    return generateCommentUseCase.generateComment(templateName, alertModel);
  }

  public Map<String, String> generateMatchComments(
      String templateName, List<MatchContext> matchesContext) {
    return generateMatchCommentUseCase.generateComment(templateName, matchesContext);
  }
}
