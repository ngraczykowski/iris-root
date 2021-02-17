package com.silenteight.simulator.management;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.dto.SimulationState;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;

import static com.silenteight.simulator.management.SimulationFixture.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { ManagementTestConfiguration.class })
class CreateSimulationUseCaseTest extends BaseDataJpaTest {

  @Autowired
  CreateSimulationUseCase underTest;

  @Autowired
  SimulationEntityRepository simulationEntityRepository;

  @Test
  void shouldCreateSimulation() {
    underTest.activate(CREATE_SIMULATION_REQUEST, USERNAME);

    Collection<SimulationEntity> simulations = simulationEntityRepository.findAll();

    assertThat(simulations).hasSize(1);
    SimulationEntity simulationEntity = new LinkedList<>(simulations).get(0);
    assertThat(simulationEntity.getSimulationId()).isEqualTo(SIMULATION_ID);
    assertThat(simulationEntity.getName()).isEqualTo(NAME);
    assertThat(simulationEntity.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(simulationEntity.getDatasetIds()).containsExactly(DATASET_ID);
    assertThat(simulationEntity.getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(simulationEntity.getState()).isEqualTo(SimulationState.PENDING);
    assertThat(simulationEntity.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(simulationEntity.getCreatedAt()).isNotNull();
    assertThat(simulationEntity.getStartedAt()).isNull();
    assertThat(simulationEntity.getFinishedAt()).isNull();
  }

  @Test
  void shouldThrowWhenUuidAlreadyExists()  {
    underTest.activate(CREATE_SIMULATION_REQUEST, USERNAME);

    assertThatThrownBy(() -> underTest.activate(CREATE_SIMULATION_REQUEST, USERNAME))
        .isInstanceOf(NonUniqueSimulationException.class);
  }
}