/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.comments.comment.port.CommentFacadePort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MockCommentFacadePort implements CommentFacadePort {

  @Override
  public String generateComment(String templateName, AlertContext alertModel) {
    return "Random comment for test purposes lol";
  }

  @Override
  public Map<String, String> generateMatchComments(
      String templateName, List<MatchContext> matchesContext) {
    return matchesContext.stream()
        .collect(Collectors.toMap(MatchContext::getMatchId, mc -> "random comment"));
  }
}
