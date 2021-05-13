package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.loader.Loader;

import java.io.Reader;

@Slf4j
public class PebbleDbLoader implements Loader<String> {

  @Override
  public Reader getReader(String cacheKey) {
    log.info("Loading Pebble cacheKey {} from Database", cacheKey);
    throw new UnsupportedOperationException("Fetching template from database required");
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
    log.info("Checking Pebble cacheKey {} from Database", templateName);
    throw new UnsupportedOperationException("Checking template from database required");
  }
}
