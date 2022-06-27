package com.silenteight.sens.webapp.audit.log;

import com.silenteight.sens.webapp.audit.domain.AuditLogService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.audit.log.AuditLogFixtures.AUDIT_DATA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogUseCaseTest {

  @InjectMocks
  private AuditLogUseCase underTest;

  @Mock
  private AuditLogService auditLogService;

  @Test
  void logAudit() {
    // when
    underTest.handle(AUDIT_DATA);

    // then
    verify(auditLogService).log(AUDIT_DATA);
  }
}
