package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;

@RequiredArgsConstructor
class DefaultLoggingErrorMessageListener implements ErrorMessageListener {

  private final LoggingHandler loggingHandler;
//
//  @Override
//  public Level getLevel() {
//    return Level.WARN;
//  }
//
//  @Override
//  public Object getErrorMessage(Message<Object> message) {
//    return String.format("The following message has been pushed to %s : %s",
//            errorQueueName,
//            message.getHeaders().toString());
//  }

  @Override
  public void handleMessage(Message<?> message) {
    loggingHandler.handleMessage(message);
  }
}
