package com.silenteight.simulator.dataset.create;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.dataset.create.exception.EmptyDatasetException;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.simulator.dataset.create.CreateDatasetRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.dataset.create.CreateDatasetRequest.PRE_AUDIT_TYPE;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATE_DATASET_REQUEST;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.DATASET;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.EMPTY_DATASET;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDatasetUseCaseTest {

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
    when(createDatasetService.createDataset(CREATE_DATASET_REQUEST)).thenReturn(DATASET);

    // when
    underTest.activate(CREATE_DATASET_REQUEST);

    // then
    verify(datasetMetadataService).createMetadata(CREATE_DATASET_REQUEST, DATASET);
    var logCaptor = forClass(AuditDataDto.class);
    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(POST_AUDIT_TYPE);
  }

  @Test
  void throwExceptionIfDatasetIsEmpty() {
    // given
    when(createDatasetService.createDataset(CREATE_DATASET_REQUEST)).thenReturn(EMPTY_DATASET);

    // then
    assertThatThrownBy(() -> underTest.activate(CREATE_DATASET_REQUEST))
        .isInstanceOf(EmptyDatasetException.class)
        .hasMessage("Dataset contains no alerts");
    verifyNoInteractions(datasetMetadataService);
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
