package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class DiscriminatorFetcherConfiguration {

  private final RecommendationOrderProperties recommendationOrderProperties;
  private final ScbBridgeConfigProperties scbBridgeConfigProperties;

  @Bean
  DiscriminatorFetcher discriminatorFetcher(
      @Qualifier("externalOnDemandDataSource") DataSource dataSource) {
    return new DiscriminatorFetcher(dataSource, fetcherConfiguration(), dateConverter());
  }

  private DateConverter dateConverter() {
    return new DateConverter(scbBridgeConfigProperties.getTimeZone());
  }

  private FetcherConfiguration fetcherConfiguration() {
    return new FetcherConfiguration(
        recommendationOrderProperties.getDbRelationName(),
        scbBridgeConfigProperties.getQueryTimeout());
  }
}
