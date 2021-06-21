package com.silenteight.adjudication.engine.comments.comment;

import com.mitchellbosecke.pebble.error.LoaderException;

@SuppressWarnings("java:S110")
public class TemplateNotFoundException extends LoaderException {

  private static final long serialVersionUID = 1L;

  TemplateNotFoundException(String templateName) {
    super(null, "Could not find template \"" + templateName + "\"");
  }
}
