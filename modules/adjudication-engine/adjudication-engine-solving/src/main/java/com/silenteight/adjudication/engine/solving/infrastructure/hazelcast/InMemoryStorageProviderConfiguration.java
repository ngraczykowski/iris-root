/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.hazelcast;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.infrastructure.hazelcast.HazelcastConfigurationProperties.AsyncBackup;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastConfigurationProperties.class)
class InMemoryStorageProviderConfiguration {

  private static final String ALERT_MAP = "in-memory-alert";
  private static final String MATCH_FEATURES_MAP = "in-memory-match-features";
  public static final String ALERT_CATEGORY_VALUE_QUEUE = "in-memory-alert-category-value";
  public static final String ALERT_COMMENTS_INPUTS_QUEUE = "in-memory-alert-comments-inputs";
  public static final String AE_GOVERNANCE_MATCH_TO_SEND_QUEUE =
      "in-memory-ae-governance-match-to-send";
  public static final String AE_GOVERNANCE_ALERT_TO_SEND_QUEUE =
      "in-memory-ae-governance-alert-to-send";
  private final HazelcastConfigurationProperties hazelcastConfigurationProperties;

  @Bean
  HazelcastInstance hazelcastInstance() {
    log.info("Initialize Application InMemoryStorage");
    Config inMemoryConfig = new Config();
    inMemoryConfig.setClusterName("in-memory-alert-processing");
    inMemoryConfig.getSerializationConfig().getCompactSerializationConfig().setEnabled(true);
    return Hazelcast.newHazelcastInstance(inMemoryConfig);
  }

  @Bean
  Queue<AlertSolutionRequest> governanceAlertsToSendQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(AE_GOVERNANCE_ALERT_TO_SEND_QUEUE);
  }

  @Bean
  Queue<MatchSolutionRequest> governanceMatchToSendQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(AE_GOVERNANCE_MATCH_TO_SEND_QUEUE);
  }

  @Bean
  Queue<String> alertCommentsInputQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(ALERT_COMMENTS_INPUTS_QUEUE);
  }

  @Bean
  Queue<Long> alertCategoryValuesQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(ALERT_CATEGORY_VALUE_QUEUE);
  }

  @Bean
  AlertSolvingRepository inMemoryAlertStorageProvider(final HazelcastInstance hazelcastInstance) {
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
