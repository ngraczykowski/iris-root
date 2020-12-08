package com.silenteight.sep.base.common.messaging;

import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.Message;

public interface ErrorMessageLogger {

  Level getLevel();

  Object getErrorMessage(Message<Object> message);
}
