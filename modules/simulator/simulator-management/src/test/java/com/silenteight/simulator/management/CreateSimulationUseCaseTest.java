package com.silenteight.simulator.management;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.simulator.management.CreateSimulationRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.management.CreateSimulationRequest.PRE_AUDIT_TYPE;
import static com.silenteight.simulator.management.SimulationFixtures.ANALYSIS;
import static com.silenteight.simulator.management.SimulationFixtures.CREATE_SIMULATION_REQUEST;
import static com.silenteight.simulator.management.SimulationFixtures.SOLVING_MODEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSimulationUseCaseTest {

  @InjectMocks
  private CreateSimulationUseCase underTest;

  @Mock
  private ModelService modelService;

  @Mock
  private AnalysisService analysisService;

  @Mock
  private SimulationService simulationService;

  @Mock
  private AuditingLogger auditingLogger;

  @Test
  void createSimulation() {
    // given
    when(modelService.getModel(CREATE_SIMULATION_REQUEST.getModelName())).thenReturn(SOLVING_MODEL);
    when(analysisService.createAnalysis(SOLVING_MODEL)).thenReturn(ANALYSIS);

    // when
    underTest.activate(CREATE_SIMULATION_REQUEST);

    // then
    verify(simulationService).createSimulation(CREATE_SIMULATION_REQUEST, ANALYSIS.getName());
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