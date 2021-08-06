package com.silenteight.simulator.dataset.archive;

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

import static com.silenteight.simulator.dataset.archive.ArchiveDatasetRequest.POST_AUDIT_TYPE;
import static com.silenteight.simulator.dataset.archive.ArchiveDatasetRequest.PRE_AUDIT_TYPE;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ARCHIVE_DATASET_REQUEST;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchiveDatasetUseCaseTest {

  @InjectMocks
  private ArchiveDatasetUseCase underTest;

  @Mock
  private DatasetMetadataService datasetMetadataService;

  @Mock
  private AuditingLogger auditingLogger;

  @Test
  void archiveDataset() {
    // when
    underTest.activate(ARCHIVE_DATASET_REQUEST);

    // then
    verify(datasetMetadataService).archive(ID);
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
