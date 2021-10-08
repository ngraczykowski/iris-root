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
import static com.silenteight.simulator.management.domain.SimulationState.CANCELED;
import static com.silenteight.simulator.management.domain.SimulationState.DONE;
import static com.silenteight.simulator.management.domain.SimulationState.PENDING;
import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
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
    Optional<SimulationEntity> simulationOpt =
        simulationRepository.findByAnalysisName(ANALYSIS_NAME);
    assertThat(simulationOpt).isPresent();
    SimulationEntity simulation = simulationOpt.get();
    assertThat(simulation.getState()).isEqualTo(RUNNING);
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
  void shouldCancel() {
    // given
    SimulationEntity simulation = persistSimulation(PENDING);

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

  private SimulationEntity persistSimulation() {
    return persistSimulation(PENDING_STATE);
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
        .modelName(MODEL_NAME)
        .analysisName(ANALYSIS_NAME)
        .build();

    return simulationRepository.save(simulationEntity);
  }
}
