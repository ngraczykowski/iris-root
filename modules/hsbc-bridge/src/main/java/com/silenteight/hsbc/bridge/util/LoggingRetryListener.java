package com.silenteight.hsbc.bridge.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingRetryListener extends RetryListenerSupport {

  @Override
  public <T, E extends Throwable> void close(
      RetryContext context,
      RetryCallback<T, E> callback, Throwable throwable) {

    log.error("Unable to recover from Exception");
    log.error("Error", throwable);
    super.close(context, callback, throwable);
  }

  @Override
  public <T, E extends Throwable> void onError(
      RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

    log.error("Exception Occurred, Retry Count {}", context.getRetryCount());
    super.onError(context, callback, throwable);
  }

  @Override
  public <T, E extends Throwable> boolean open(
      RetryContext context,
      RetryCallback<T, E> callback) {

    log.error("Exception Occurred, Retry Session Started");
    return super.open(context, callback);
  }
}
