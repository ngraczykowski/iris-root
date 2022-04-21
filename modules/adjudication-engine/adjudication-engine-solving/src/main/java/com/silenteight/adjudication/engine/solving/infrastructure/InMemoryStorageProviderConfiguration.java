package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.*;
import com.silenteight.adjudication.engine.solving.infrastructure.configuration.HazelcastConfigurationProperties;
import com.silenteight.adjudication.engine.solving.infrastructure.configuration.HazelcastConfigurationProperties.AsyncBackup;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
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
      final RabbitTemplate rabbitTemplate
  ) {

    final HazelcastConfigurationProperties hazelcastConfigurationProperties =
        new HazelcastConfigurationProperties();
    final AsyncBackup asyncBackup = new AsyncBackup();
    hazelcastConfigurationProperties.setAsyncBackup(asyncBackup);
    //    hazelcastConfigurationProperties.setTtl(Duration.ofSeconds(30));
    hazelcastConfigurationProperties.setEnableReadBackup(true);

    EvictionConfig evictionConfig = new EvictionConfig()
        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
        .setEvictionPolicy(EvictionPolicy.LRU);

    hazelcastInstance.getConfig()
        .addMapConfig(new MapConfig()
            .setName(ALERT_MAP)
            .setMaxIdleSeconds(3600)
            .setEvictionConfig(evictionConfig)
            .setAsyncBackupCount(
                hazelcastConfigurationProperties.getAsyncBackup().getMinAsyncBackups())
            .setReadBackupData(hazelcastConfigurationProperties.isEnableReadBackup()));
    //            .setTimeToLiveSeconds(hazelcastConfigurationProperties.getTtl().toSecondsPart()));
    final IMap<Long, AlertSolving> map = hazelcastInstance.getMap(ALERT_MAP);
    log.info("Registering Entry Eviction listener");
    map.addEntryListener(new InMemoryAlertStorageEventListener(), true);
    //TODO make it bean - and move configuration to properties
    final EventStoreConfigurationProperties eventStoreConfigurationProperties =
        new EventStoreConfigurationProperties(
            "test",
            "test"
        );
    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(6);
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

    hazelcastInstance.getConfig()
        .addMapConfig(new MapConfig()
            .setName(MATCH_FEATURES_MAP)
            .setAsyncBackupCount(
                hazelcastConfigurationProperties.getAsyncBackup().getMinAsyncBackups())
            .setReadBackupData(hazelcastConfigurationProperties.isEnableReadBackup()));
    final IMap<MatchFeatureKey, MatchFeature> map = hazelcastInstance.getMap(MATCH_FEATURES_MAP);
    log.info("Registering Entry Eviction listener for {}", MATCH_FEATURES_MAP);
    map.addEntryListener(new InMemoryAlertStorageEventListener(), true);

    return new InMemoryMatchFeaturesRepository(map);
  }
}
