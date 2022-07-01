package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableConfigurationProperties(PublisherConfigurationProperties.class)
@RequiredArgsConstructor
class PublisherConfiguration {

  private final PublisherConfigurationProperties properties;

  @Bean
  public TaskExecutor inMemorySolvingExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(properties.getCorePoolSize());
    executor.setMaxPoolSize(properties.getMaxPoolSize());
    executor.setQueueCapacity(properties.getQueueCapacity());
    executor.setThreadNamePrefix("solving-executor-");
    return executor;
  }
}
