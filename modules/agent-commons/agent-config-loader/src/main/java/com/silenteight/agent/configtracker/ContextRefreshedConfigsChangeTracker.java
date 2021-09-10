package com.silenteight.agent.configtracker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.configloader.AgentConfigs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class ContextRefreshedConfigsChangeTracker {

  private static final String APPLICATION_NAME = "spring.application.name";
  private static final String APPLICATION_CONFIG_DIR = "application.config.dir";

  private final ConfigsChangedEventPublisher publisher;
  private final List<ConfigsLoader<?>> configLoaders;

  @EventListener(ContextRefreshedEvent.class)
  public void onRefreshContext(ContextRefreshedEvent event) {
    log.info("Reloading agents configurations...");

    var configDir = readConfigDir(event.getApplicationContext());

    configLoaders.forEach(loader -> {
      var configs = loader.load(configDir);
      publisher.publish(configsChangedEvent(loader.getPropertiesType(), configs));
    });
  }

  private static String readConfigDir(ApplicationContext context) {
    var env = context.getEnvironment();
    return ofNullable(env.getProperty(APPLICATION_CONFIG_DIR))
        .orElse(env.getRequiredProperty(APPLICATION_NAME));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static ConfigsChangedEvent<?> configsChangedEvent(
      Class<?> propertiesType, AgentConfigs<?> configs) {
    return new ConfigsChangedEvent(propertiesType, configs);
  }
}
