package com.silenteight.adjudication.engine.comments.comment;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.utils.PathUtils;
import org.springframework.stereotype.Component;

import java.io.Reader;

@Slf4j
@RequiredArgsConstructor
@Component
class PebbleCommentTemplateLoader implements Loader<String> {

  private final CommentTemplateRepository repository;

  @Setter
  private char expectedSeparator = '/';

  private String prefix;

  private String suffix = ".peb";

  @Override
  public Reader getReader(@NonNull String templateName) {
    log.debug("Loading template from the database: templateName={}", templateName);

    return repository
        .findFirstByTemplateNameOrderByRevisionDesc(getFullName(templateName))
        .map(CommentTemplate::toReader)
        .orElseThrow(() -> new PebbleTemplateNotFoundException(templateName));
  }

  // NOTE(ahaczewski): Copy-pasted from the Pepple ClasspathLoader class.
  private String getFullName(String templateName) {
    StringBuilder path = new StringBuilder(128);
    if (prefix != null) {

      path.append(prefix);

      if (!prefix.endsWith(Character.toString(this.expectedSeparator))) {
        path.append(this.expectedSeparator);
      }
    }

    path.append(templateName);

    if (suffix != null) {
      path.append(suffix);
    }

    return path.toString();
  }

  @Override
  public void setCharset(String charset) {
    // NOTE(ahaczewski): Not applicable.
  }

  @Override
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  @Override
  public String resolveRelativePath(String relativePath, String anchorPath) {
    return PathUtils.resolveRelativePath(relativePath, anchorPath, expectedSeparator);
  }

  @Override
  public String createCacheKey(String templateName) {
    return templateName;
  }

  @Override
  public boolean resourceExists(String templateName) {
    return repository.findFirstByTemplateNameOrderByRevisionDesc(getFullName(templateName))
        .isPresent();
  }
}
