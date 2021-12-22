package com.silenteight.simulator.management.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
import static com.silenteight.simulator.management.domain.SimulationState.STREAMING;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { SimulationTestConfiguration.class })
class SimulationQueryTest extends BaseDataJpaTest {

  @Autowired
  SimulationQuery underTest;

  @Autowired
  SimulationRepository simulationRepository;

  @Test
  void shouldListSimulationsByStates() {
    // given
    UUID canceledSimulationId = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
    persistSimulation(ID, PENDING_STATE);
    persistSimulation(canceledSimulationId, CANCELED_STATE);

    // when
    List<SimulationDto> result = underTest.list(of(PENDING_STATE, ARCHIVED_STATE));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
  }

  @Test
  void shouldListSimulationsByStatesStreamingCase() {
    // given
    UUID canceledSimulationId = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
    persistSimulation(ID, STREAMING);
    persistSimulation(canceledSimulationId, CANCELED_STATE);

    // when
    List<SimulationDto> result = underTest.list(of(RUNNING, ARCHIVED_STATE));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), RUNNING);
  }

  @Test
  void shouldFindSimulationByModel() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    List<SimulationDto> result = underTest.findByModel(MODEL_NAME);

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
  }

  @Test
  void shouldFindSimulationsByModels() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    List<SimulationDto> result = underTest.findByModels(of(MODEL_NAME));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
  }

  @Test
  void shouldThrowIfSimulationsNotFoundByModel() {
    assertThatThrownBy(() -> underTest.findByModel(MODEL_NAME))
        .isInstanceOf(InvalidModelNameException.class)
        .hasMessageContaining("modelName=" + MODEL_NAME);
  }

  @Test
  void shouldGetSimulationDetailsById() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    SimulationDetailsDto result = underTest.get(ID);

    // then
    assertSimulationDetails(result);
  }

  @Test
  void shouldThrowIfSimulationNotFoundById() {
    assertThatThrownBy(() -> underTest.get(ID))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("simulationId=" + ID);
  }

  @Test
  void shouldGetSimulationDetailsByAnalysisName() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    SimulationDetailsDto result = underTest.get(ANALYSIS_NAME);

    // then
    assertSimulationDetails(result);
  }

  @Test
  void shouldThrowIfSimulationNotFoundByAnalysisName() {
    assertThatThrownBy(() -> underTest.get(ANALYSIS_NAME))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("analysisName=" + ANALYSIS_NAME);
  }

  @Test
  void shouldGetAnalysisNames() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    Collection<String> result = underTest.getAnalysisNames(DATASETS);

    // then
    assertThat(result).isEqualTo(List.of(ANALYSIS_NAME));
  }

  @Test
  void shouldReturnRunningForStreamingStatus() {
    // given
    persistSimulation(ID, STREAMING);

    // when
    SimulationDetailsDto simulationDetailsDto = underTest.get(ID);

    // then
    assertThat(simulationDetailsDto.getState()).isEqualTo(RUNNING);
  }

  private static void assertSimulation(SimulationDto result, SimulationState pendingState) {
    assertThat(result.getId()).isEqualTo(ID);
    assertThat(result.getSimulationName()).isEqualTo(SIMULATION_NAME);
    assertThat(result.getState()).isEqualTo(pendingState);
    assertThat(result.getDatasets()).isEqualTo(DATASETS);
    assertThat(result.getModel()).isEqualTo(MODEL_NAME);
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt()).isNotNull();
  }

  private static void assertSimulationDetails(SimulationDetailsDto result) {
    assertThat(result.getId()).isEqualTo(ID);
    assertThat(result.getSimulationName()).isEqualTo(SIMULATION_NAME);
    assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(result.getState()).isEqualTo(PENDING_STATE);
    assertThat(result.getDatasets()).isEqualTo(DATASETS);
    assertThat(result.getModel()).isEqualTo(MODEL_NAME);
    assertThat(result.getAnalysis()).isEqualTo(ANALYSIS_NAME);
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt()).isNotNull();
  }

  private void persistSimulation(UUID simulationId, SimulationState state) {
    SimulationEntity simulationEntity = SimulationEntity
        .builder()
        .simulationId(simulationId)
        .name(SIMULATION_NAME)
        .description(DESCRIPTION)
        .state(state)
        .createdBy(USERNAME)
        .datasetNames(DATASETS)
        .modelName(MODEL_NAME)
        .analysisName(ANALYSIS_NAME)
        .build();

    simulationRepository.save(simulationEntity);
  }
}
