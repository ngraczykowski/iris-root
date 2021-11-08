package com.silenteight.simulator.retention.dataset.expired;

import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.list.ListSimulationsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatasetExpiredConfiguration {

  @Bean
  DatasetsExpiredUseCase expireDatasetUseCase(
      DatasetMetadataService datasetMetadataService,
      DatasetQuery datasetQuery,
      ListSimulationsQuery listSimulationsQuery) {

    return new DatasetsExpiredUseCase(datasetMetadataService, datasetQuery, listSimulationsQuery);
  }
}
