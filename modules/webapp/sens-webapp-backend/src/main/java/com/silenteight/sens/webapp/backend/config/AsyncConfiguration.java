package com.silenteight.sens.webapp.backend.config;

import com.silenteight.commons.concurrent.ExecutorUtils;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

  @Override
  public Executor getAsyncExecutor() {
    return ExecutorUtils.createExecutorService();
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
