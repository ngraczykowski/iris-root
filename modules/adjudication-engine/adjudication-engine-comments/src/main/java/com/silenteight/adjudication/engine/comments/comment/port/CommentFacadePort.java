/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.comments.comment.port;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

import java.util.List;
import java.util.Map;

public interface CommentFacadePort {

  String generateComment(String templateName, AlertContext alertModel);

  Map<String, String> generateMatchComments(String templateName, List<MatchContext> matchesContext);
}
