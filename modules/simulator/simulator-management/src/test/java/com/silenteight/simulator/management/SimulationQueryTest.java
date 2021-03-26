package com.silenteight.simulator.management;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.dto.SimulationDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.simulator.management.SimulationFixture.*;
import static com.silenteight.simulator.management.dto.SimulationState.PENDING;
import static java.util.Arrays.asList;
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

    List<SimulationDto> result = underTest.listAllSimulations();

    assertThat(result).hasSize(1);
    SimulationDto simulationDto = result.get(0);
    assertThat(simulationDto.getId()).isEqualTo(SIMULATION_ID);
    assertThat(simulationDto.getName()).isEqualTo(NAME);
    assertThat(simulationDto.getStatus()).isEqualTo(PENDING);
    assertThat(simulationDto.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(simulationDto.getCreatedAt()).isNotNull();
    assertThat(simulationDto.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(simulationDto.getModelName()).isEqualTo(MODEL_NAME);
  }

  private SimulationEntity persistSimulation() {
    SimulationEntity simulationEntity = SimulationEntity.builder()
        .simulationId(SIMULATION_ID)
        .name(NAME)
        .state(STATUS)
        .createdBy(USERNAME)
        .datasetNames(of(DATASET_NAME))
        .modelName(MODEL_NAME)
        .analysisName(ANALYSIS_NAME)
        .build();

    return simulationEntityRepository.save(simulationEntity);
  }

  private static <T> Set<T> of(T...t) {
    return new HashSet<>(asList(t));
  }
}