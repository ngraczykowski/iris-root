package com.silenteight.adjudication.engine.comments.comment;

interface TemplateEngineWithCache extends TemplateEngine {

  void invalidateCache();
}
