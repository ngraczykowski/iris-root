package com.silenteight.sens.webapp.backend.changerequest.cancel;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.sens.webapp.backend.changerequest.cancel.CancelChangeRequestMessageHandlerFixtures.CANCEL_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelChangeRequestMessageHandlerTest {

  @InjectMocks
  private CancelChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void handleCancelMessage_changeRequestCancelled() {
    // given
    when(changeRequestService.cancel(
        CANCEL_MESSAGE.getChangeRequestId(),
        CANCEL_MESSAGE.getCancellerUsername(),
        toOffsetDateTime(CANCEL_MESSAGE.getCancelledAt()))).thenReturn(UUID.randomUUID());

    // when
    underTest.handle(CANCEL_MESSAGE);

    // then
    assertThat(RequestCorrelation.id()).isEqualTo(toJavaUuid(CANCEL_MESSAGE.getCorrelationId()));
    verify(changeRequestService).cancel(
        CANCEL_MESSAGE.getChangeRequestId(),
        CANCEL_MESSAGE.getCancellerUsername(),
        toOffsetDateTime(CANCEL_MESSAGE.getCancelledAt()));
  }
}
