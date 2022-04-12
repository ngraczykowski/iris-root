package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
class GenerateMatchCommentUseCase {

  private final TemplateEngineRegistry registry;

  @Transactional(readOnly = true)
  Map<String, String> generateComment(
      String alertTemplateName, List<MatchContext> matchContexts) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Generating comment: templateName={} for {} matches",
          alertTemplateName, matchContexts.size());
    }
    try {
      return generateMatchComments(alertTemplateName, matchContexts);
    } catch (Exception e) {
      log.error(
          "Error occurred while generating comment: templateName={}",
          alertTemplateName, e);
      //TODO(iwnek) the proper error state should be set in
      // com.silenteight.adjudication.api.v1.Recommendation message
      return Map.of();
    }
  }

  private Map<String, String> generateMatchComments(
      String matchTemplateName, List<MatchContext> matchContexts) {
    var comments = new HashMap<String, String>();

    for (var matchContext : matchContexts) {
      var comment =
          registry.find(matchTemplateName).generateComment(matchTemplateName, matchContext);
      comments.put(matchContext.getMatchId(), comment);
    }

    return comments;
  }
}
