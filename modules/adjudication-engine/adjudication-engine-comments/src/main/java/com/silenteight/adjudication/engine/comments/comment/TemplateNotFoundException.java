package com.silenteight.adjudication.engine.comments.comment;

public class TemplateNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  TemplateNotFoundException(String message) {
    super(message);
  }
}
