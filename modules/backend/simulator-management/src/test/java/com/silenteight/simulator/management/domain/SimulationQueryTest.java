package com.silenteight.simulator.management.domain;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;
import com.silenteight.simulator.management.list.dto.SimulationListDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { SimulationConfiguration.class })
class SimulationQueryTest extends BaseDataJpaTest {

  @Autowired
  SimulationQuery underTest;

  @Autowired
  SimulationRepository simulationRepository;

  @MockBean
  TimeSource timeSource;

  @Test
  void shouldListSimulationsByStates() {
    // given
    UUID canceledSimulationId = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
    persistSimulation(ID, PENDING_STATE);
    persistSimulation(canceledSimulationId, CANCELED_STATE);

    // when
    List<SimulationListDto> result = underTest.list(of(PENDING_STATE, ARCHIVED_STATE));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
  }

  @Test
  void shouldFindSimulationByModel() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    List<SimulationListDto> result = underTest.findByModels(of(MODEL_NAME));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
  }

  @Test
  void shouldFindSimulationsByModels() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    List<SimulationListDto> result = underTest.findByModels(of(MODEL_NAME));

    // then
    assertThat(result).hasSize(1);
    assertSimulation(result.get(0), PENDING_STATE);
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
    SimulationDetailsDto result = underTest.get(ANALYSIS_NAME_1);

    // then
    assertSimulationDetails(result);
  }

  @Test
  void shouldThrowIfSimulationNotFoundByAnalysisName() {
    assertThatThrownBy(() -> underTest.get(ANALYSIS_NAME_1))
        .isInstanceOf(SimulationNotFoundException.class)
        .hasMessageContaining("analysisName=" + ANALYSIS_NAME_1);
  }

  @Test
  void shouldGetAnalysisNames() {
    // given
    persistSimulation(ID, PENDING_STATE);

    // when
    Collection<String> result = underTest.getAnalysisNames(DATASETS);

    // then
    assertThat(result).isEqualTo(of(ANALYSIS_NAME_1));
  }

  @Test
  void shouldGetAnalysisName() {
    //given
    persistSimulation(ID, PENDING_STATE);

    //when
    String analysisName = underTest.getAnalysisName(ID);

    //them
    assertThat(analysisName).isEqualTo(ANALYSIS_NAME_1);
  }

  private static void assertSimulation(SimulationListDto result, SimulationState pendingState) {
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
    assertThat(result.getAnalysis()).isEqualTo(ANALYSIS_NAME_1);
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getSolvedAlerts()).isEqualTo(SOLVED_ALERTS);
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
        .analysisName(ANALYSIS_NAME_1)
        .solvedAlerts(SOLVED_ALERTS)
        .build();

    simulationRepository.save(simulationEntity);
  }
}
