package com.silenteight.searpaymentsmockup;

import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;
import org.springframework.messaging.Message;

@Slf4j(topic = "sear-payments-mockup.log-message-advice")
class RequestHandlerLogMessageAdvice extends AbstractRequestHandlerAdvice {

  @Override
  protected Object doInvoke(
      ExecutionCallback callback, Object target, Message<?> message) {
    try {
      return callback.execute();
    } finally {
      log.info("Message processed: {}", message);
    }
  }
}
