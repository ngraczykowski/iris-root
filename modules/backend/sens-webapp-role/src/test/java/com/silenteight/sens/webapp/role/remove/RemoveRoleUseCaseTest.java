package com.silenteight.sens.webapp.role.remove;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.role.RoleTestFixtures.REMOVE_ROLE_REQUEST;
import static com.silenteight.sens.webapp.role.RoleTestFixtures.ROLE_ID_1;
import static com.silenteight.sens.webapp.role.remove.RemoveRoleRequest.POST_AUDIT_TYPE;
import static com.silenteight.sens.webapp.role.remove.RemoveRoleRequest.PRE_AUDIT_TYPE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveRoleUseCaseTest {

  @InjectMocks
  private RemoveRoleUseCase underTest;

  @Mock
  private RoleService roleService;

  @Mock
  private AuditingLogger auditingLogger;

  @Test
  void createRole() {
    // when
    underTest.activate(REMOVE_ROLE_REQUEST);

    // then
    verify(roleService).remove(ROLE_ID_1);
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
