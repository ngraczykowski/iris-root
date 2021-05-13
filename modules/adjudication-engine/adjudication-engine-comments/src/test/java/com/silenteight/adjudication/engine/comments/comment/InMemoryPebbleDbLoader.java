package com.silenteight.adjudication.engine.comments.comment;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

class InMemoryPebbleDbLoader extends PebbleDbLoader {

  private final Map<String, String> templates;

  InMemoryPebbleDbLoader(Map<String, String> templates) {
    this.templates = templates;
  }

  @Override
  public Reader getReader(String cacheKey) {
    return new StringReader(templates.get(cacheKey));
  }

  @Override
  public boolean resourceExists(String templateName) {
    return this.templates.containsKey(templateName);
  }
}
