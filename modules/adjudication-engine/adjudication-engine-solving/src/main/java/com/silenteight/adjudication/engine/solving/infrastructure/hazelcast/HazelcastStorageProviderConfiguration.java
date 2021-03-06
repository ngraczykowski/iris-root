/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.hazelcast;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;
import com.silenteight.adjudication.engine.solving.infrastructure.hazelcast.HazelcastConfigurationProperties.AsyncBackup;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Queue;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(HazelcastConfigurationProperties.class)
@ConditionalOnProperty(
    value = "ae.solving.engine",
    havingValue = "HAZELCAST",
    matchIfMissing = true)
class HazelcastStorageProviderConfiguration {

  private static final String ALERT_MAP = "in-memory-alert";
  private static final String COMMENT_INPUT_MAP = "in-memory-comment-inputs";

  private static final String MATCH_FEATURES_MAP = "in-memory-match-features";
  public static final String ALERT_CATEGORY_VALUE_QUEUE = "in-memory-alert-category-value";
  public static final String ALERT_COMMENTS_INPUTS_QUEUE = "in-memory-alert-comments-inputs";
  public static final String ALERT_COMMENTS_INPUTS_STORE_QUEUE =
      "in-memory-alert-comments-inputs-store";
  public static final String AE_GOVERNANCE_MATCH_TO_SEND_QUEUE =
      "in-memory-ae-governance-match-to-send";
  public static final String AE_GOVERNANCE_ALERT_TO_SEND_QUEUE =
      "in-memory-ae-governance-alert-to-send";
  private static final String MATCH_CATEGORY_STORE_QUEUE = "in-memory-match-category-value-store";
  private static final String MATCH_FEATURE_STORE_QUEUE = "in-memory-match-feature-value-store";
  private static final String MATCH_SOLUTION_STORE_QUEUE = "in-memory-match-solution-store";
  private final HazelcastConfigurationProperties hazelcastConfigurationProperties;

  @Bean
  HazelcastInstance hazelcastInstance() {
    log.info(
        "Initialize Application InMemoryStorage with clusterName {}",
        hazelcastConfigurationProperties.getClusterName());
    Config inMemoryConfig = new Config();
    inMemoryConfig.setClusterName(hazelcastConfigurationProperties.getClusterName());
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
  Queue<CommentInput> alertCommentsInputStoreQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(ALERT_COMMENTS_INPUTS_STORE_QUEUE);
  }

  @Bean
  Queue<Long> alertCategoryValuesQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(ALERT_CATEGORY_VALUE_QUEUE);
  }

  @Bean
  Queue<MatchCategory> matchCategoryStoreQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(MATCH_CATEGORY_STORE_QUEUE);
  }

  @Bean
  Queue<MatchFeatureValue> matchFeatureStoreQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(MATCH_FEATURE_STORE_QUEUE);
  }

  @Bean
  Queue<MatchSolution> matchSolutionStoreQueue(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getQueue(MATCH_SOLUTION_STORE_QUEUE);
  }

  @Bean
  Map<Long, String>  commentInputMap(HazelcastInstance hazelcastInstance) {
    return hazelcastInstance.getMap(COMMENT_INPUT_MAP);
  }

  @Bean
  AlertSolvingRepository inMemoryAlertStorageProvider(final HazelcastInstance hazelcastInstance) {
    hazelcastInstance
        .getConfig()
        .addMapConfig(createAlertMapConfig(ALERT_MAP,hazelcastConfigurationProperties))
        .addMapConfig(createAlertMapConfig(COMMENT_INPUT_MAP,hazelcastConfigurationProperties));

    final IMap<Long, AlertSolving> map = hazelcastInstance.getMap(ALERT_MAP);
    log.info("Registering Entry Eviction listener");

    return new HazelcastAlertSolvingRepository(map);
  }

  private static MapConfig createAlertMapConfig(String mapName,
      HazelcastConfigurationProperties hazelcastConfigurationProperties) {
    return new MapConfig()
        .setName(mapName)
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

    return new HazelcastMatchFeaturesRepository(map);
  }
}
