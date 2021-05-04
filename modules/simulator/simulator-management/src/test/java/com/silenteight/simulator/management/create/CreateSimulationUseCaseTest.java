package com.silenteight.simulator.management.create;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.domain.SimulationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.create.CreateSimulationRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.management.create.CreateSimulationRequest.PRE_AUDIT_TYPE;
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
  private DatasetQuery datasetQuery;

  @Mock
  private SimulationService simulationService;

  @Mock
  private AuditingLogger auditingLogger;

  @Test
  void createSimulation() {
    // given
    when(modelService.getModel(CREATE_SIMULATION_REQUEST.getModel())).thenReturn(SOLVING_MODEL);
    when(analysisService.createAnalysis(SOLVING_MODEL)).thenReturn(ANALYSIS);
    when(datasetQuery.getExternalResourceName(DATASET_ID)).thenReturn(DATASET_EXTERNAL_NAME);

    // when
    underTest.activate(CREATE_SIMULATION_REQUEST);

    // then
    verify(analysisService).addDatasetToAnalysis(ANALYSIS.getName(), DATASET_EXTERNAL_NAME);
    verify(simulationService).createSimulation(
        CREATE_SIMULATION_REQUEST, DATASETS, ANALYSIS.getName());
    var logCaptor = forClass(AuditDataDto.class);
    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(POST_AUDIT_TYPE);
  }

  private static AuditDataDto getPreAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    return getAudit(logCaptor, 0);
  }

  private static AuditDataDto getPostAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    return getAudit(logCaptor, 1);
  }

  private static AuditDataDto getAudit(ArgumentCaptor<AuditDataDto> logCaptor, int index) {
    List<AuditDataDto> logs = logCaptor.getAllValues();
    assertThat(logs).hasSizeGreaterThanOrEqualTo(index + 1);
    return logs.get(index);
  }
}
