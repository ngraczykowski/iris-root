package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
class ConfigsChangeTrackerConfiguration {

  private final List<ConfigsChangedListener<?>> listeners;
  private final List<ConfigsLoader<?>> configLoaders;

  @Bean
  ConfigsChangedEventPublisher configsChangedEventPublisher() {
    return new ConfigsChangedEventPublisher(listeners);
  }

  @Bean
  ContextRefreshedConfigsChangeTracker configsChangeTracker(
      ConfigsChangedEventPublisher publisher) {
    return new ContextRefreshedConfigsChangeTracker(publisher, configLoaders);
  }

}
