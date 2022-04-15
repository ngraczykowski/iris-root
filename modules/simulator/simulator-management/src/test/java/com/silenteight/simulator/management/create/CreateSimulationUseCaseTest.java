package com.silenteight.simulator.management.create;

import com.silenteight.adjudication.api.v1.Analysis;
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
    Analysis analysis = createAnalysis(ANALYSIS_NAME_3,0);
    when(modelService.getModel(CREATE_SIMULATION_REQUEST.getModel())).thenReturn(SOLVING_MODEL);
    when(analysisService.createAnalysis(SOLVING_MODEL)).thenReturn(analysis);
    when(datasetQuery.getExternalResourceName(DATASET_ID_1)).thenReturn(DATASET_NAME_1);
    when(datasetQuery.getExternalResourceName(DATASET_ID_2)).thenReturn(DATASET_NAME_2);

    // when
    underTest.activate(CREATE_SIMULATION_REQUEST);

    // then
    verify(analysisService).addDatasetToAnalysis(analysis.getName(), DATASET_NAME_1);
    verify(analysisService).addDatasetToAnalysis(analysis.getName(), DATASET_NAME_2);
    verify(simulationService).createSimulation(
        CREATE_SIMULATION_REQUEST, DATASETS, analysis.getName());
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
