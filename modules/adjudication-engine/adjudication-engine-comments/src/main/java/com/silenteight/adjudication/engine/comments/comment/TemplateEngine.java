package com.silenteight.adjudication.engine.comments.comment;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

interface TemplateEngine {

  boolean templateExists(String templateName);

  String generateComment(String templateName, AlertContext alertContext);
}
