package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.REJECTED;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.*;
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
    assertThat(preAudit.getType()).isEqualTo(AddChangeRequestRequest.PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(AddChangeRequestRequest.POST_AUDIT_TYPE);
  }

  @Test
  void approveChangeRequest() {
    // given
    persistChangeRequest();

    // when
    underTest.approve(CHANGE_REQUEST_ID, APPROVED_BY, APPROVER_COMMENT);

    // then
    Optional<ChangeRequest> changeRequestOpt = repository.findByChangeRequestId(CHANGE_REQUEST_ID);
    assertThat(changeRequestOpt).isNotEmpty();
    ChangeRequest changeRequest = changeRequestOpt.get();
    assertThat(changeRequest.getState()).isEqualTo(APPROVED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVED_BY);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(APPROVER_COMMENT);

    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(ApproveChangeRequestRequest.PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(ApproveChangeRequestRequest.POST_AUDIT_TYPE);
  }

  @Test
  void throwExceptionWhenApprovingNotExistingChangeRequest() {
    assertThatThrownBy(() -> underTest.approve(CHANGE_REQUEST_ID, APPROVED_BY, APPROVER_COMMENT))
        .isInstanceOf(ChangeRequestNotFoundException.class);
  }

  @Test
  void rejectChangeRequest() {
    // given
    persistChangeRequest();

    // when
    underTest.reject(CHANGE_REQUEST_ID, REJECTED_BY, REJECTOR_COMMENT);

    // then
    Optional<ChangeRequest> changeRequestOpt = repository.findByChangeRequestId(CHANGE_REQUEST_ID);
    assertThat(changeRequestOpt).isNotEmpty();
    ChangeRequest changeRequest = changeRequestOpt.get();
    assertThat(changeRequest.getState()).isEqualTo(REJECTED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(REJECTED_BY);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(REJECTOR_COMMENT);

    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(RejectChangeRequestRequest.PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(RejectChangeRequestRequest.POST_AUDIT_TYPE);
  }

  @Test
  void throwExceptionWhenRejectingNotExistingChangeRequest() {
    assertThatThrownBy(() -> underTest.reject(CHANGE_REQUEST_ID, REJECTED_BY, REJECTOR_COMMENT))
        .isInstanceOf(ChangeRequestNotFoundException.class);
  }

  @Test
  void cancelChangeRequest() {
    // given
    persistChangeRequest();

    // when
    underTest.cancel(CHANGE_REQUEST_ID, CANCELLED_BY);

    // then
    Optional<ChangeRequest> changeRequestOpt = repository.findByChangeRequestId(CHANGE_REQUEST_ID);
    assertThat(changeRequestOpt).isNotEmpty();
    ChangeRequest changeRequest = changeRequestOpt.get();
    assertThat(changeRequest.getState()).isEqualTo(CANCELLED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(CANCELLED_BY);

    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(CancelChangeRequestRequest.PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(CancelChangeRequestRequest.POST_AUDIT_TYPE);
  }

  @Test
  void throwExceptionWhenCancellingNotExistingChangeRequest() {
    assertThatThrownBy(() -> underTest.cancel(CHANGE_REQUEST_ID, CANCELLED_BY))
        .isInstanceOf(ChangeRequestNotFoundException.class);
  }

  @Test
  void shouldGetChangeRequestState() {
    persistChangeRequest();

    ChangeRequestState changeRequestState =
        underTest.getChangeRequestState(CHANGE_REQUEST_ID);

    assertThat(changeRequestState).isEqualTo(PENDING);
  }

  private void persistChangeRequest() {
    repository.save(new ChangeRequest(CHANGE_REQUEST_ID, MODEL_NAME, CREATED_BY, CREATOR_COMMENT));
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
