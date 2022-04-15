package com.silenteight.simulator.management.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.domain.SimulationState.*;
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
    underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME_1);

    // then
    Optional<SimulationEntity> simulationOpt =
        simulationRepository.findByAnalysisName(ANALYSIS_NAME_1);
    assertThat(simulationOpt).isPresent();
    SimulationEntity simulation = simulationOpt.get();
    assertThat(simulation.getState()).isEqualTo(RUNNING);
  }

  @Test
  void shouldThrowNonUniqueSimulationException() {
    // given
    underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME_1);

    // when + then
    assertThatThrownBy(
        () -> underTest.createSimulation(CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS_NAME_1))
        .isInstanceOf(NonUniqueSimulationException.class)
        .hasMessageContaining("simulationId=" + CREATE_SIMULATION_REQUEST.getId());
  }

  @Test
  void exists() {
    // given
    persistSimulation();

    // when
    boolean result = underTest.exists(ANALYSIS_NAME_1);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void doesNotExist() {
    // when
    boolean result = underTest.exists(ANALYSIS_NAME_1);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void shouldFinish() {
    // given
    SimulationEntity simulation = persistSimulation(STREAMING, ANALYSIS_NAME_1, SOLVED_ALERTS);

    // when
    underTest.finish(ANALYSIS_NAME_1);

    // then
    SimulationEntity savedSimulation =
        entityManager.find(SimulationEntity.class, simulation.getId());
    assertThat(savedSimulation.getState()).isEqualTo(DONE);
  }

  @Test
  void shouldNotFinishWhenArchived() {
    // given
    SimulationEntity simulation = persistSimulation(ARCHIVED, ANALYSIS_NAME_1, SOLVED_ALERTS);

    // when
    underTest.finish(ANALYSIS_NAME_1);

    // then
    SimulationEntity savedSimulation =
        entityManager.find(SimulationEntity.class, simulation.getId());
    assertThat(savedSimulation.getState()).isEqualTo(ARCHIVED);
  }

  @Test
  void shouldThrowIfFinishingAndSimulationNotFound() {
    assertThatThrownBy(() -> underTest.finish(ANALYSIS_NAME_1))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("analysisName=" + ANALYSIS_NAME_1);
  }

  @Test
  void shouldCancel() {
    // given
    SimulationEntity simulation = persistSimulation(PENDING, ANALYSIS_NAME_1, SOLVED_ALERTS);

    // when
    underTest.cancel(ID);

    // then
    SimulationEntity savedSimulation =
        entityManager.find(SimulationEntity.class, simulation.getId());
    assertThat(savedSimulation.getState()).isEqualTo(CANCELED);
  }

  @Test
  void shouldThrowIfCancelingAndSimulationNotFound() {
    assertThatThrownBy(() -> underTest.cancel(ID))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("simulationId=" + ID);
  }

  @Test
  void shouldArchive() {
    // given
    SimulationEntity simulation = persistSimulation(DONE, ANALYSIS_NAME_1, SOLVED_ALERTS);

    // when
    underTest.archive(ID);

    // then
    SimulationEntity savedSimulation =
        entityManager.find(SimulationEntity.class, simulation.getId());
    assertThat(savedSimulation.getState()).isEqualTo(ARCHIVED);
  }

  @Test
  void shouldThrowIfArchivingAndSimulationNotFound() {
    assertThatThrownBy(() -> underTest.archive(ID))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("simulationId=" + ID);
  }

  @Test
  void shouldTimeoutSimulation() {
    //given
    SimulationEntity simulationEntity = persistSimulation(RUNNING, ANALYSIS_NAME_1, 100L);

    //when
    underTest.timeout(ID);

    //then
    SimulationEntity simulation =
        entityManager.find(SimulationEntity.class, simulationEntity.getId());

    assertThat(simulation.getState()).isEqualTo(ERROR);
  }

  private SimulationEntity persistSimulation() {
    return persistSimulation(PENDING_STATE, ANALYSIS_NAME_1, SOLVED_ALERTS);
  }

  private SimulationEntity persistSimulation(
      SimulationState state, String analysisName, long solvedAlerts) {

    SimulationEntity simulationEntity = SimulationEntity
        .builder()
        .simulationId(ID)
        .name(SIMULATION_NAME)
        .description(DESCRIPTION)
        .state(state)
        .createdBy(USERNAME)
        .datasetNames(DATASETS)
        .modelName(MODEL_NAME)
        .analysisName(analysisName)
        .solvedAlerts(solvedAlerts)
        .build();

    return simulationRepository.save(simulationEntity);
  }
}
