package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.SystemIdFinder.FinderConfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class SystemIdFinderConfiguration {

  private final ScbBridgeAlertOrderProperties alertOrderProperties;
  private final ScbBridgeConfigProperties configProperties;

  @Bean
  SystemIdFinder systemIdFinder(
      @Qualifier("externalOnDemandDataSource") DataSource externalDataSource) {
    FinderConfiguration configuration = FinderConfiguration.of(
        alertOrderProperties.getDbRelationName(),
        externalDataSource,
        configProperties.getQueryTimeout()
    );
    return new SystemIdFinder(configuration);
  }
}
