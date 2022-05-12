package com.silenteight.warehouse.common.web.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;
import javax.validation.Valid;

@EnableAsync
@Configuration
@EnableConfigurationProperties(AsyncThreadPoolProperties.class)
class AsyncConfiguration implements AsyncConfigurer {

  @Autowired
  @Qualifier("asyncAuthAwareThreadPoolTaskExecutor")
  private ThreadPoolTaskExecutor delegate;

  @Bean
  public ThreadPoolTaskExecutor asyncAuthAwareThreadPoolTaskExecutor(
      @Valid AsyncThreadPoolProperties properties) {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(properties.getCoreSize());
    executor.setMaxPoolSize(properties.getMaxSize());
    executor.setQueueCapacity(properties.getQueueCapacity());
    executor.setThreadNamePrefix(properties.getThreadNamePrefix());
    return executor;
  }

  @Bean
  @Override
  public Executor getAsyncExecutor() {
    return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
