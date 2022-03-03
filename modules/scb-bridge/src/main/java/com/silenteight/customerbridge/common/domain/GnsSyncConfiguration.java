package com.silenteight.customerbridge.common.domain;

import com.silenteight.customerbridge.common.config.SyncDataSourcesConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@Configuration
@Import(SyncDataSourcesConfiguration.class)
class GnsSyncConfiguration {

  @Bean
  GnsSyncService gnsSyncService(GnsSyncRepository syncRepository) {
    return new GnsSyncService(syncRepository);
  }

  @Bean
  GnsSyncDeltaService gnsSyncDeltaService(
      EntityManager entityManager,
      GnsSyncDeltaRepository deltaRepository) {

    return new GnsSyncDeltaService(entityManager, deltaRepository);
  }
}
