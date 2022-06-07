/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.hazelcast;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.infrastructure.hazelcast.HazelcastConfigurationProperties.AsyncBackup;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class InMemoryStorageProviderConfiguration {

  private static final String ALERT_MAP = "in-memory-alert";
  private static final String MATCH_FEATURES_MAP = "in-memory-match-features";

  @Bean
  HazelcastInstance hazelcastInstance() {
    log.info("Initialize Application InMemoryStorage");
    Config inMemoryConfig = new Config();
    inMemoryConfig.setClusterName("in-memory-alert-processing");
    inMemoryConfig.getSerializationConfig().getCompactSerializationConfig().setEnabled(true);
    return Hazelcast.newHazelcastInstance(inMemoryConfig);
  }

  @Bean
  AlertSolvingRepository inMemoryAlertStorageProvider(final HazelcastInstance hazelcastInstance) {

    final HazelcastConfigurationProperties hazelcastConfigurationProperties =
        new HazelcastConfigurationProperties();
    final AsyncBackup asyncBackup = new AsyncBackup();
    hazelcastConfigurationProperties.setAsyncBackup(asyncBackup);
    hazelcastConfigurationProperties.setEnableReadBackup(true);

    hazelcastInstance
        .getConfig()
        .addMapConfig(createAlertMapConfig(hazelcastConfigurationProperties));

    final IMap<Long, AlertSolving> map = hazelcastInstance.getMap(ALERT_MAP);
    log.info("Registering Entry Eviction listener");

    return new InMemoryAlertSolvingRepository(map);
  }

  private static MapConfig createAlertMapConfig(
      HazelcastConfigurationProperties hazelcastConfigurationProperties) {
    return new MapConfig()
        .setName(ALERT_MAP)
        .setMaxIdleSeconds(hazelcastConfigurationProperties.getTtlMinutes().toSecondsPart())
        .setAsyncBackupCount(hazelcastConfigurationProperties.getAsyncBackup().getMinAsyncBackups())
        .setReadBackupData(true)
        .setEvictionConfig(
            hazelcastConfigurationProperties.getAlertEvictionConfig().getEvictionConfig());
  }

  @Bean
  MatchFeaturesRepository inMemoryMatchFeaturesStorageProvider(
      final HazelcastInstance hazelcastInstance) {

    final HazelcastConfigurationProperties hazelcastConfigurationProperties =
        new HazelcastConfigurationProperties();
    final AsyncBackup asyncBackup = new AsyncBackup();
    hazelcastConfigurationProperties.setAsyncBackup(asyncBackup);
    hazelcastConfigurationProperties.setEnableReadBackup(true);

    final IMap<MatchFeatureKey, MatchFeature> map = hazelcastInstance.getMap(MATCH_FEATURES_MAP);
    log.info("Registering Entry Eviction listener for {}", MATCH_FEATURES_MAP);

    return new InMemoryMatchFeaturesRepository(map);
  }
}
