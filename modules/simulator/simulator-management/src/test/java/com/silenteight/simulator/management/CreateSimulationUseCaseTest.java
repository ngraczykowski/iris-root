package com.silenteight.simulator.management;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.dto.SimulationState;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.silenteight.simulator.management.CreateSimulationRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.management.CreateSimulationRequest.PRE_AUDIT_TYPE;
import static com.silenteight.simulator.management.SimulationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { ManagementTestConfiguration.class })
class CreateSimulationUseCaseTest extends BaseDataJpaTest {

  @Autowired
  CreateSimulationUseCase underTest;

  @Autowired
  SimulationEntityRepository simulationEntityRepository;

  @Autowired
  AuditingLogger auditingLogger;

  @Test
  void shouldCreateSimulation() {
    underTest.activate(CREATE_SIMULATION_REQUEST);

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
    underTest.activate(CREATE_SIMULATION_REQUEST);

    assertThatThrownBy(() -> underTest.activate(CREATE_SIMULATION_REQUEST))
        .isInstanceOf(NonUniqueSimulationException.class);
  }

  @Test
  void shouldCreateAuditEntry() {
    Mockito.reset(auditingLogger);

    underTest.activate(CREATE_SIMULATION_REQUEST);

    var logCaptor = forClass(AuditDataDto.class);
    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(POST_AUDIT_TYPE);
  }

  private AuditDataDto getPreAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    List<AuditDataDto> logs = logCaptor.getAllValues();
    assertThat(logs).hasSizeGreaterThanOrEqualTo(1);
    return logs.get(0);
  }

  private AuditDataDto getPostAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    List<AuditDataDto> logs = logCaptor.getAllValues();
    assertThat(logs).hasSizeGreaterThanOrEqualTo(2);
    return logs.get(1);
  }
}