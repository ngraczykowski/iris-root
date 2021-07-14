package com.silenteight.simulator.management.domain;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static com.google.common.collect.ImmutableList.of;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { SimulationTestConfiguration.class })
class SimulationServiceTest extends BaseDataJpaTest {

  @Autowired
  SimulationService underTest;

  @Autowired
  DatasetMetadataService datasetMetadataService;

  @Autowired
  SimulationEntityRepository simulationEntityRepository;

  @Test
  void shouldCountAllAlerts() {
    // given
    createDataset(DATASET_NAME_1, DATASET__ALERT_COUNT_1);
    createDataset(DATASET_NAME_2, DATASET__ALERT_COUNT_2);
    persistSimulation();

    // when
    long allAlertCount = underTest.countAllAlerts(ANALYSIS_NAME);

    // when
    assertThat(allAlertCount).isEqualTo(DATASET__ALERT_COUNT_1 + DATASET__ALERT_COUNT_2);
  }

  @Test
  void shouldThrowIfCountingAllAlertsAndSimulationNotFound() {
    assertThatThrownBy(() -> underTest.countAllAlerts(ANALYSIS_NAME))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("analysisName=" + ANALYSIS_NAME);
  }

  private void createDataset(String datasetName, long alertCount) {
    OffsetDateTime from = now();
    CreateDatasetRequest request = CreateDatasetRequest.builder()
        .id(DATASET_ID_1)
        .datasetName("Dataset 1")
        .description("Dataset 1")
        .createdBy(USERNAME)
        .rangeFrom(from)
        .rangeTo(from.plusDays(2))
        .countries(of("SG", "UK"))
        .build();
    Dataset dataset = Dataset.newBuilder()
        .setName(datasetName)
        .setAlertCount(alertCount)
        .build();

    datasetMetadataService.createMetadata(request, dataset);
  }

  private void persistSimulation() {
    SimulationEntity simulationEntity = SimulationEntity
        .builder()
        .simulationId(ID)
        .name(SIMULATION_NAME)
        .description(DESCRIPTION)
        .state(STATE)
        .createdBy(USERNAME)
        .datasetNames(DATASETS)
        .modelName(MODEL)
        .analysisName(ANALYSIS_NAME)
        .build();

    simulationEntityRepository.save(simulationEntity);
  }
}
