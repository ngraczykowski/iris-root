package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestMessageHandlerFixtures.REJECT_MESSAGE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RejectChangeRequestMessageHandlerTest {

  @InjectMocks
  private RejectChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void invokesServicePassingCommandAttributes() {
    when(changeRequestService.reject(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(REJECT_MESSAGE);

    // then
    verify(changeRequestService).reject(
        REJECT_MESSAGE.getChangeRequestId(),
        REJECT_MESSAGE.getRejectorUsername(),
        REJECT_MESSAGE.getRejectorComment(),
        toOffsetDateTime(REJECT_MESSAGE.getRejectedAt()));
  }

  @Test
  void returnsRejectBulkBranchChangeCommand() {
    // given
    UUID bulkChangeId = randomUUID();
    when(changeRequestService.reject(anyLong(), anyString(), anyString(), any()))
        .thenReturn(bulkChangeId);

    // when
    RejectBulkBranchChangeCommand command = underTest.handle(REJECT_MESSAGE);

    // then
    assertThat(command.getId()).isEqualTo(fromJavaUuid(bulkChangeId));
    assertThat(command.getCorrelationId()).isEqualTo(REJECT_MESSAGE.getCorrelationId());
  }

  @Test
  void storesCorrelationIdInThreadLocal() {
    when(changeRequestService.reject(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(REJECT_MESSAGE);

    // then
    assertThat(RequestCorrelation.id()).isEqualTo(toJavaUuid(REJECT_MESSAGE.getCorrelationId()));
  }
}
