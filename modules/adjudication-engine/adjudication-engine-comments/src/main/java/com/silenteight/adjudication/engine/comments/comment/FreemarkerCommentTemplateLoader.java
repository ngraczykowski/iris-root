package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import freemarker.cache.TemplateLoader;
import org.springframework.stereotype.Component;

import java.io.Reader;

@Slf4j
@RequiredArgsConstructor
@Component
class FreemarkerCommentTemplateLoader implements TemplateLoader {

  private final CommentTemplateRepository repository;

  @Override
  public Object findTemplateSource(String name) {
    return repository.findFirstByTemplateName(name).orElse(null);
  }

  @Override
  public long getLastModified(Object templateSource) {
    CommentTemplate template = (CommentTemplate) templateSource;
    // NOTE(iwnek) Currently templates in database are immutable
    return template.getCreatedAt().toInstant().toEpochMilli();
  }

  @Override
  public Reader getReader(Object templateSource, String encoding) {
    CommentTemplate template = (CommentTemplate) templateSource;

    log.debug("Loading template from the database: templateName={}", template.getTemplateName());
    return template.toReader();
  }

  @Override
  public void closeTemplateSource(Object templateSource) {
    // do nothing
  }
}
