package com.silenteight.adjudication.engine.comments.comment;

public class CommentGenerationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  CommentGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
