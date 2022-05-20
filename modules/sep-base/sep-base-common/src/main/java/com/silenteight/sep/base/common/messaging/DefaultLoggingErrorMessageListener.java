package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;

@RequiredArgsConstructor
class DefaultLoggingErrorMessageListener implements ErrorMessageListener {

  private final LoggingHandler loggingHandler;

  @Override
  public void handleMessage(Message<?> message) {
    loggingHandler.handleMessage(message);
  }
}
