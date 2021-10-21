package com.silenteight.payments.bridge.svb.learning.reader.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
class LearningAlertsConfig {

  public static final String LEARNING_THREAD_POOL_EXECUTOR = "learningThreadPoolTaskExecutor";

  @Bean(name = LEARNING_THREAD_POOL_EXECUTOR)
  public Executor learningThreadPoolTaskExecutor() {
    return Executors.newSingleThreadExecutor();
  }
}
