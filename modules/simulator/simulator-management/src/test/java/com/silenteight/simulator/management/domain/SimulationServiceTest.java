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
import java.util.Optional;

import static com.google.common.collect.ImmutableList.of;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.domain.SimulationState.DONE;
import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
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
  SimulationRepository simulationRepository;

  @Test
  void shouldCreateSimulation() {
    // when
    underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME);

    // then
    Optional<SimulationEntity> simulation =
        simulationRepository.findByAnalysisName(ANALYSIS_NAME);
    assertThat(simulation).isPresent();
  }

  @Test
  void exists() {
    // given
    persistSimulation();

    // when
    boolean result = underTest.exists(ANALYSIS_NAME);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void doesNotExist() {
    // when
    boolean result = underTest.exists(ANALYSIS_NAME);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void shouldThrowNonUniqueSimulationException() {
    // given
    underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME);

    // when + then
    assertThatThrownBy(
        () -> underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME))
        .isInstanceOf(NonUniqueSimulationException.class)
        .hasMessageContaining("simulationId=" + CREATE_SIMULATION_REQUEST.getId());
  }

  @Test
  void shouldFinish() {
    // given
    SimulationEntity simulation = persistSimulation(RUNNING);

    // when
    underTest.finish(ANALYSIS_NAME);

    // then
    SimulationEntity savedSimulation =
        entityManager.find(SimulationEntity.class, simulation.getId());
    assertThat(savedSimulation.getState()).isEqualTo(DONE);
  }

  @Test
  void shouldThrowIfFinishingAndSimulationNotFound() {
    assertThatThrownBy(() -> underTest.finish(ANALYSIS_NAME))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("analysisName=" + ANALYSIS_NAME);
  }

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

  private SimulationEntity persistSimulation() {
    return persistSimulation(STATE);
  }

  private SimulationEntity persistSimulation(SimulationState state) {
    SimulationEntity simulationEntity = SimulationEntity
        .builder()
        .simulationId(ID)
        .name(SIMULATION_NAME)
        .description(DESCRIPTION)
        .state(state)
        .createdBy(USERNAME)
        .datasetNames(DATASETS)
        .modelName(MODEL)
        .analysisName(ANALYSIS_NAME)
        .build();

    return simulationRepository.save(simulationEntity);
  }
}
