package com.silenteight.sens.webapp.backend.changerequest.cancel;

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
import static com.silenteight.sens.webapp.backend.changerequest.cancel.CancelChangeRequestMessageHandlerFixtures.CANCEL_MESSAGE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelChangeRequestMessageHandlerTest {

  @InjectMocks
  private CancelChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void invokesServicePassingCommandAttributes() {
    when(changeRequestService.cancel(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(CANCEL_MESSAGE);

    // then
    verify(changeRequestService).cancel(
        CANCEL_MESSAGE.getChangeRequestId(),
        CANCEL_MESSAGE.getCancellerUsername(),
        CANCEL_MESSAGE.getCancellerComment(),
        toOffsetDateTime(CANCEL_MESSAGE.getCancelledAt()));
  }

  @Test
  void returnsRejectBulkBranchChangeCommand() {
    // given
    UUID bulkChangeId = randomUUID();
    when(changeRequestService.cancel(anyLong(), anyString(), anyString(), any()))
        .thenReturn(bulkChangeId);

    // when
    RejectBulkBranchChangeCommand command = underTest.handle(CANCEL_MESSAGE);

    // then
    assertThat(command.getId()).isEqualTo(fromJavaUuid(bulkChangeId));
    assertThat(command.getCorrelationId()).isEqualTo(CANCEL_MESSAGE.getCorrelationId());
  }

  @Test
  void storesCorrelationIdInThreadLocal() {
    when(changeRequestService.cancel(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(CANCEL_MESSAGE);

    // then
    assertThat(RequestCorrelation.id()).isEqualTo(toJavaUuid(CANCEL_MESSAGE.getCorrelationId()));
  }
}
