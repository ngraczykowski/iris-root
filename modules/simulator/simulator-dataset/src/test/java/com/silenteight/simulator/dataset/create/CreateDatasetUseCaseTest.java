package com.silenteight.simulator.dataset.create;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.simulator.dataset.create.CreateDatasetFixtures.CREATE_DATASET_REQUEST;
import static com.silenteight.simulator.dataset.create.CreateDatasetRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.dataset.create.CreateDatasetRequest.PRE_AUDIT_TYPE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDatasetUseCaseTest {

  private static final String EXTERNAL_RESOURCE_NAME = "datasets/1234";

  @InjectMocks
  private CreateDatasetUseCase underTest;

  @Mock
  private CreateDatasetService createDatasetService;

  @Mock
  private DatasetMetadataService datasetMetadataService;

  @Mock
  private AuditingLogger auditingLogger;

  @Test
  void createDataset() {
    // given
    long alertsCount = 5L;
    Dataset dataset = makeDataset(EXTERNAL_RESOURCE_NAME, alertsCount);
    when(createDatasetService.createDataset(CREATE_DATASET_REQUEST)).thenReturn(dataset);

    // when
    underTest.activate(CREATE_DATASET_REQUEST);

    // then
    verify(datasetMetadataService).createMetadata(CREATE_DATASET_REQUEST, dataset);
    var logCaptor = forClass(AuditDataDto.class);
    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(POST_AUDIT_TYPE);
  }

  private static Dataset makeDataset(String datasetName, long alertCount) {
    return Dataset.newBuilder()
        .setName(datasetName)
        .setAlertCount(alertCount)
        .build();
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
