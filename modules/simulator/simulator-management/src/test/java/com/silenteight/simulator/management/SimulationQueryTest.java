package com.silenteight.simulator.management;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.dto.SimulationDto;

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
@ContextConfiguration(classes = { ManagementTestConfiguration.class })
class SimulationQueryTest extends BaseDataJpaTest {

  @Autowired
  SimulationQuery underTest;

  @Autowired
  SimulationEntityRepository simulationEntityRepository;

  @Test
  void shouldListSimulations() {
    persistSimulation();

    List<SimulationDto> result = underTest.list();

    assertThat(result).hasSize(1);
    SimulationDto simulationDto = result.get(0);
    assertThat(simulationDto.getId()).isEqualTo(SIMULATION_ID);
    assertThat(simulationDto.getSimulationName()).isEqualTo(SIMULATION_NAME);
    assertThat(simulationDto.getState()).isEqualTo(STATE);
    assertThat(simulationDto.getDatasetNames()).isEqualTo(DATASET_NAMES);
    assertThat(simulationDto.getModelName()).isEqualTo(MODEL_NAME);
    assertThat(simulationDto.getProgressState()).isEqualTo(PROGRESS_STATE);
    assertThat(simulationDto.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(simulationDto.getCreatedAt()).isNotNull();
  }

  private void persistSimulation() {
    SimulationEntity simulationEntity = SimulationEntity
        .builder()
        .simulationId(SIMULATION_ID)
        .name(SIMULATION_NAME)
        .state(STATE)
        .createdBy(USERNAME)
        .datasetNames(DATASET_NAMES)
        .modelName(MODEL_NAME)
        .analysisName(ANALYSIS_NAME)
        .build();

    simulationEntityRepository.save(simulationEntity);
  }
}
