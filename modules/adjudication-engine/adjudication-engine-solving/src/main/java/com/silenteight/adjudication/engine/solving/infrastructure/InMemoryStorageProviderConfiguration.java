package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.infrastructure.configuration.HazelcastConfigurationProperties;
import com.silenteight.adjudication.engine.solving.infrastructure.configuration.HazelcastConfigurationProperties.AsyncBackup;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
@EnableConfigurationProperties(EventStoreConfigurationProperties.class)
class InMemoryStorageProviderConfiguration {

  private static final String ALERT_MAP = "in-memory-alert";
  private static final String MATCH_FEATURES_MAP = "in-memory-match-features";
  private static final String MATCH_FEA = "in-memory-match-features";

  @Bean
  HazelcastInstance hazelcastInstance() {
    log.info("Initialize Application InMemoryStorage");
    Config inMemoryConfig = new Config();
    inMemoryConfig.setClusterName("in-memory-alert-processing");
    inMemoryConfig.getSerializationConfig().getCompactSerializationConfig().setEnabled(true);
    return Hazelcast.newHazelcastInstance(inMemoryConfig);
  }

  @Bean
  AlertSolvingRepository inMemoryAlertStorageProvider(
      final HazelcastInstance hazelcastInstance,
      final RabbitTemplate rabbitTemplate,
      final EventStoreConfigurationProperties eventStoreConfigurationProperties
  ) {

    final HazelcastConfigurationProperties hazelcastConfigurationProperties =
        new HazelcastConfigurationProperties();
    final AsyncBackup asyncBackup = new AsyncBackup();
    hazelcastConfigurationProperties.setAsyncBackup(asyncBackup);
    hazelcastConfigurationProperties.setEnableReadBackup(true);

    final IMap<Long, AlertSolving> map = hazelcastInstance.getMap(ALERT_MAP);
    log.info("Registering Entry Eviction listener");
    final ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(eventStoreConfigurationProperties.getPoolSize());
    final EventStore eventStore =
        new EventStore(rabbitTemplate, eventStoreConfigurationProperties, hazelcastInstance,
            scheduledExecutorService
        );

    return new InMemoryAlertSolvingRepository(eventStore, map);
  }

  @Bean
  MatchFeaturesRepository inMemoryMatchFeaturesStorageProvider(
      final HazelcastInstance hazelcastInstance
  ) {

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
