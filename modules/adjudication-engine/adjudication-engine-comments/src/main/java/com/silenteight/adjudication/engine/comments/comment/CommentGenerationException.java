package com.silenteight.adjudication.engine.comments.comment;

public class CommentGenerationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  CommentGenerationException(String templateName, Throwable cause) {
    super("Could not generate comment from template " + templateName, cause);
  }
}
