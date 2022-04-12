package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

interface TemplateEngine {

  boolean templateExists(String templateName);

  String generateComment(String templateName, AlertContext alertContext);

  String generateComment(String templateName, MatchContext alertContext);
}
