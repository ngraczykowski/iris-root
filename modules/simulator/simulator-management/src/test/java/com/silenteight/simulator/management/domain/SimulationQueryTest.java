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

import java.util.List;

import static com.silenteight.simulator.management.SimulationFixtures.*;
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
  void shouldListSimulations() {
    persistSimulation();

    List<SimulationDto> result = underTest.list();

    assertThat(result).hasSize(1);
    SimulationDto simulationDto = result.get(0);
    assertThat(simulationDto.getId()).isEqualTo(ID);
    assertThat(simulationDto.getSimulationName()).isEqualTo(SIMULATION_NAME);
    assertThat(simulationDto.getState()).isEqualTo(STATE);
    assertThat(simulationDto.getDatasets()).isEqualTo(DATASETS);
    assertThat(simulationDto.getModel()).isEqualTo(MODEL);
    assertThat(simulationDto.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(simulationDto.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldGetSimulationDetailsById() {
    // given
    persistSimulation();

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
    persistSimulation();

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

  private static void assertSimulationDetails(SimulationDetailsDto result) {
    assertThat(result.getId()).isEqualTo(ID);
    assertThat(result.getSimulationName()).isEqualTo(SIMULATION_NAME);
    assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(result.getState()).isEqualTo(STATE);
    assertThat(result.getDatasets()).isEqualTo(DATASETS);
    assertThat(result.getModel()).isEqualTo(MODEL);
    assertThat(result.getAnalysis()).isEqualTo(ANALYSIS_NAME);
    assertThat(result.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(result.getCreatedAt()).isNotNull();
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

    simulationRepository.save(simulationEntity);
  }
}
