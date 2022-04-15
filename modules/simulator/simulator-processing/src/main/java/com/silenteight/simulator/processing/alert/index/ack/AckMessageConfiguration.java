package com.silenteight.simulator.processing.alert.index.ack;

import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AckMessageConfiguration {

  @Bean
  FetchAckMessageUseCase fetchAckMessageUseCase(
      AnalysisService analysisService,
      IndexedAlertService indexedAlertService,
      SimulationDetailsQuery simulationQuery,
      DatasetMetadataService datasetService,
      SimulationService simulationService,
      IndexedAlertQuery indexedAlertQuery) {

    return new FetchAckMessageUseCase(
        indexedAlertService,
        simulationService,
        indexedAlertQuery);
  }
}
