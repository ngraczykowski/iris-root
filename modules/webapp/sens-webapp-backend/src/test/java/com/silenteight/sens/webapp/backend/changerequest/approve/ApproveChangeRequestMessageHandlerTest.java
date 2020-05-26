package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandlerFixtures.APPROVE_MESSAGE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveChangeRequestMessageHandlerTest {

  @InjectMocks
  private ApproveChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void handleApproveMessage_changeRequestApproved() {
    // when
    underTest.handle(APPROVE_MESSAGE);

    // then
    verify(changeRequestService).approve(
        APPROVE_MESSAGE.getChangeRequestId(),
        APPROVE_MESSAGE.getApproverUsername(),
        toOffsetDateTime(APPROVE_MESSAGE.getApprovedAt()));
  }
}
