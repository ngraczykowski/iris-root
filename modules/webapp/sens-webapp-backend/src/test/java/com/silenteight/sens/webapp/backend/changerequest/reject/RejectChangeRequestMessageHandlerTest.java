package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestMessageHandlerFixtures.REJECT_MESSAGE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RejectChangeRequestMessageHandlerTest {

  @InjectMocks
  private RejectChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void handleRejectMessage_changeRequestRejected() {
    // when
    underTest.handle(REJECT_MESSAGE);

    // then
    verify(changeRequestService).reject(
        REJECT_MESSAGE.getChangeRequestId(),
        REJECT_MESSAGE.getRejectorUsername(),
        toOffsetDateTime(REJECT_MESSAGE.getRejectedAt()));
  }
}
