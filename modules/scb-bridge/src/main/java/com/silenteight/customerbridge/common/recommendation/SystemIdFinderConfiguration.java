package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.customerbridge.common.recommendation.SystemIdFinder.FinderConfiguration;

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
