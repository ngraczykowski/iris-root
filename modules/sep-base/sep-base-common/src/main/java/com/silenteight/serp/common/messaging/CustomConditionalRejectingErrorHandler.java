package com.silenteight.serp.common.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;

import static com.google.common.base.Throwables.getRootCause;

class CustomConditionalRejectingErrorHandler extends ConditionalRejectingErrorHandler {

  // NOTE(ahaczewski): Making the logger name consistent with other log messages from super class.
  private final Logger log
      = LoggerFactory.getLogger(ConditionalRejectingErrorHandler.class);  // NOSONAR

  private final FatalExceptionStrategy exceptionStrategy;

  CustomConditionalRejectingErrorHandler(FatalExceptionStrategy exceptionStrategy) {
    super(exceptionStrategy);
    this.exceptionStrategy = exceptionStrategy;
  }

  @Override
  protected void log(Throwable t) {
    Throwable rootCause = getRootCause(t);

    if (exceptionStrategy.isFatal(t)) {
      if (log.isErrorEnabled()) {
        log.error("Fatal message processing failure.", rootCause);
      }
    } else {
      if (log.isWarnEnabled()) {
        log.warn("Processing of the message failed: cause={}, message={}",
            rootCause.getClass(), rootCause.getMessage());
      }
    }
  }
}
