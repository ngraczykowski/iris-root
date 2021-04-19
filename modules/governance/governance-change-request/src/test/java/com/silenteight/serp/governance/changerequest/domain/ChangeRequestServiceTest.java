package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.AddChangeRequestRequest.POST_AUDIT_TYPE;
import static com.silenteight.serp.governance.changerequest.domain.AddChangeRequestRequest.PRE_AUDIT_TYPE;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATED_BY;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATOR_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRequestServiceTest {

  private final ChangeRequestRepository repository = new InMemoryChangeRequestRepository();
  @Mock
  private AuditingLogger auditingLogger;

  private ChangeRequestService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ChangeRequestDomainConfiguration()
        .changeRequestService(repository, auditingLogger);
  }

  @Test
  void createChangeRequest() {
    // when
    UUID changeRequestId = underTest.addChangeRequest(
        CHANGE_REQUEST_ID, MODEL_NAME, CREATED_BY, CREATOR_COMMENT);

    // then
    assertThat(changeRequestId).isEqualTo(CHANGE_REQUEST_ID);

    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

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
