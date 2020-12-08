package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.Message;

@RequiredArgsConstructor
class DefaultErrorMessageLogger implements ErrorMessageLogger {

  private final String errorQueueName;

  public Level getLevel() {
    return Level.WARN;
  }

  public Object getErrorMessage(Message<Object> message) {
    return String.format("The following message has been pushed to %s : %s",
            errorQueueName,
            message.getHeaders().toString());

  }
}
