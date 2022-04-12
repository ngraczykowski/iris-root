package com.silenteight.adjudication.engine.solving.storage;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolvingModel;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class InMemoryStorageProviderConfiguration {

  private static final String ALERT_MAP = "in-memory-alert";

  @Bean
  HazelcastInstance hazelcastInstance() {
    log.info("Initialize Application InMemoryStorage");
    Config inMemoryConfig = new Config();
    inMemoryConfig.setClusterName("in-memory-alert-processing");
    return Hazelcast.newHazelcastInstance(inMemoryConfig);
  }

  @Bean
  InMemoryAlertStorageProvider inMemoryAlertStorageProvider(HazelcastInstance hazelcastInstance) {
    IMap<String, AlertSolvingModel> map = hazelcastInstance.getMap(ALERT_MAP);
    log.info("Registering Entry Eviction listener");
    map.addEntryListener(new InMemoryAlertStorageEventListener(), true);
    return new InMemoryAlertStorageProvider(map);
  }
}
