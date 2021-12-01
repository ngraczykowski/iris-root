package com.silenteight.warehouse.retention.production.personalinformation;

import com.silenteight.warehouse.indexer.production.v2.ProductionAlertIndexV2UseCase;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;
import com.silenteight.warehouse.indexer.query.single.SingleAlertQueryConfiguration;
import com.silenteight.warehouse.retention.production.AlertDiscriminatorResolvingService;
import com.silenteight.warehouse.retention.production.MatchDiscriminatorResolvingService;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Valid;

@Configuration
@Import(SingleAlertQueryConfiguration.class)
@EnableConfigurationProperties(PersonalInformationProperties.class)
class RetentionPersonalInformationConfiguration {

  @Bean
  ErasePersonalInformationUseCase eraseDecisionCommentUseCase(
      AlertDiscriminatorResolvingService alertDiscriminatorResolvingService,
      MatchDiscriminatorResolvingService matchDiscriminatorResolvingService,
      ProductionAlertIndexV2UseCase productionAlertIndexUseCase,
      @Valid PersonalInformationProperties properties) {

    return new ErasePersonalInformationUseCase(
        alertDiscriminatorResolvingService,
        matchDiscriminatorResolvingService,
        productionAlertIndexUseCase,
        properties.getBatchSize(),
        properties.getFieldsToErase());
  }

  @Bean
  AlertDiscriminatorResolvingService alertDiscriminatorResolvingService(
      RestHighLevelClient restHighLevelAdminClient,
      ProductionSearchRequestBuilder productionSearchRequestBuilder,
      AlertSearchService alertSearchService) {

    return new AlertDiscriminatorResolvingService(restHighLevelAdminClient,
        productionSearchRequestBuilder, alertSearchService);
  }

  @Bean
  MatchDiscriminatorResolvingService matchDiscriminatorResolvingService(
      RestHighLevelClient restHighLevelAdminClient,
      ProductionSearchRequestBuilder productionMatchSearchRequestBuilder,
      AlertSearchService alertSearchService) {

    return new MatchDiscriminatorResolvingService(restHighLevelAdminClient,
        productionMatchSearchRequestBuilder, alertSearchService);
  }
}
