package com.silenteight.adjudication.engine.comments.comment;

import com.mitchellbosecke.pebble.error.LoaderException;

@SuppressWarnings("java:S110")
public class PebbleTemplateNotFoundException extends LoaderException {

  private static final long serialVersionUID = 1L;

  PebbleTemplateNotFoundException(String templateName) {
    super(null, "Could not find template \"" + templateName + "\"");
  }
}
