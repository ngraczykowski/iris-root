package com.silenteight.sens.webapp.audit.trace;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseAuditTracerTest {

  @Mock
  private AuditingLogger auditingLoggerMock;

  @Test
  void shouldSerializeOffSetDateTimeToString() {
    //given
    DatabaseAuditTracer databaseAuditTracer = new DatabaseAuditTracer(auditingLoggerMock);
    ZoneOffset zoneOffset = ZoneOffset.ofHours(2);
    OffsetDateTime dateUnderTest = OffsetDateTime.of(2020, 1, 1, 16, 0, 0, 0, zoneOffset);
    String expectedSerializedString = "\"2020-01-01T16:00:00+02:00\"";
    AuditEvent auditEvent = new AuditEvent("TEST", dateUnderTest);
    ArgumentCaptor<AuditDataDto> argumentCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    //when
    databaseAuditTracer.save(auditEvent);

    //then
    verify(auditingLoggerMock).log(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getDetails()).isEqualTo(expectedSerializedString);
  }

}
