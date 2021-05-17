package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.loader.Loader;

import java.io.Reader;
import java.io.StringReader;

@Slf4j
@RequiredArgsConstructor
class CommentTemplateLoader implements Loader<String> {

  private final CommentTemplateRepository repository;

  @Override
  public Reader getReader(String cacheKey) {
    log.debug("Load template from database using templateName:{}", cacheKey);
    var commentTemplate =
        repository.findFirstByTemplateNameOrderByRevisionDesc(cacheKey);
    if (commentTemplate.isPresent()) {
      log.info(
          "Got template:{} with revision:{}", cacheKey, commentTemplate.get().getRevision());
      return new StringReader(commentTemplate.get().getTemplate());
    }
    throw new TemplateNotFoundException(cacheKey);
  }

  @Override
  public void setCharset(String charset) {

  }

  @Override
  public void setPrefix(String prefix) {

  }

  @Override
  public void setSuffix(String suffix) {

  }

  @Override
  public String resolveRelativePath(String relativePath, String anchorPath) {
    return null;
  }

  @Override
  public String createCacheKey(String templateName) {
    return templateName;
  }

  @Override
  public boolean resourceExists(String templateName) {
    throw new UnsupportedOperationException();
  }
}
