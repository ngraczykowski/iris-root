/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.guava;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(GuavaConfigurationProperties.class)
@ConditionalOnProperty(value = "ae.solving.engine", havingValue = "GUAVA")
class GuavaStorageProviderConfiguration {

  private final GuavaConfigurationProperties guavaConfigurationProperties;

  @Bean
  Queue<AlertSolutionRequest> governanceAlertsToSendQueue() {
    return new PriorityQueue<>(guavaConfigurationProperties.getInitialQueueCapacity());
  }

  @Bean
  Queue<MatchSolutionRequest> governanceMatchToSendQueue() {
    return new PriorityQueue<>(guavaConfigurationProperties.getInitialQueueCapacity());
  }

  @Bean
  Queue<String> alertCommentsInputQueue() {
    return new PriorityQueue<>(guavaConfigurationProperties.getInitialQueueCapacity());
  }

  @Bean
  Queue<CommentInput> alertCommentsInputStoreQueue() {
    return new PriorityQueue<>(guavaConfigurationProperties.getInitialQueueCapacity());
  }

  @Bean
  Queue<Long> alertCategoryValuesQueue() {
    return new PriorityQueue<>(guavaConfigurationProperties.getInitialQueueCapacity());
  }

  @Bean
  AlertSolvingRepository inMemoryAlertStorageProvider() {
    var cache =
        CacheBuilder.newBuilder()
            .maximumSize(guavaConfigurationProperties.getAlertEvictionConfig().getMaxCapacity())
            .expireAfterWrite(
                guavaConfigurationProperties.getTtlMinutes().toMinutes(), TimeUnit.MINUTES)
            .removalListener(
                notification ->
                    log.trace(
                        "Removed {} cause:{}", notification.getKey(), notification.getCause()))
            .<Long, AlertSolving>build();

    return new GuavaAlertSolvingRepository(cache);
  }

  @Bean
  MatchFeaturesRepository inMemoryMatchFeaturesStorageProvider() {
    var cache =
        CacheBuilder.newBuilder()
            .maximumSize(guavaConfigurationProperties.getAlertEvictionConfig().getMaxCapacity())
            .expireAfterWrite(
                guavaConfigurationProperties.getTtlMinutes().toMinutes(), TimeUnit.MINUTES)
            .removalListener(
                notification ->
                    log.trace(
                        "Removed {} cause:{}", notification.getKey(), notification.getCause()))
            .<MatchFeatureKey, MatchFeature>build();

    return new GuavaMatchFeaturesRepository(cache);
  }
}
