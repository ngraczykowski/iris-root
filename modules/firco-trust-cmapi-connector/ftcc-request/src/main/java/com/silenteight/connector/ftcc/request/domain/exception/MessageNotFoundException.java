package com.silenteight.connector.ftcc.request.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class MessageNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6878602047053256900L;

  public MessageNotFoundException(@NonNull UUID batchId, @NonNull UUID messageId) {
    super(format("Message with batchId=%s and messageId=%s not found.", batchId, messageId));
  }
}
