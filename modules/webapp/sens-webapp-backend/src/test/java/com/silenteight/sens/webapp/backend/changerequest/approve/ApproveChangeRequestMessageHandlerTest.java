package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
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
import static com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandlerFixtures.APPROVE_MESSAGE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveChangeRequestMessageHandlerTest {

  @InjectMocks
  private ApproveChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void invokesServicePassingCommandAttributes() {
    when(changeRequestService.approve(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(APPROVE_MESSAGE);

    // then
    verify(changeRequestService).approve(
        APPROVE_MESSAGE.getChangeRequestId(),
        APPROVE_MESSAGE.getApproverUsername(),
        APPROVE_MESSAGE.getApproverComment(),
        toOffsetDateTime(APPROVE_MESSAGE.getApprovedAt()));
  }

  @Test
  void returnsApplyBulkBranchChangeCommand() {
    // given
    UUID bulkChangeId = randomUUID();
    when(changeRequestService.approve(anyLong(), anyString(), anyString(), any()))
        .thenReturn(bulkChangeId);

    // when
    ApplyBulkBranchChangeCommand command = underTest.handle(APPROVE_MESSAGE);

    // then
    assertThat(command.getId()).isEqualTo(fromJavaUuid(bulkChangeId));
    assertThat(command.getCorrelationId()).isEqualTo(APPROVE_MESSAGE.getCorrelationId());
  }

  @Test
  void storesCorrelationIdInThreadLocal() {
    when(changeRequestService.approve(anyLong(), anyString(), anyString(), any()))
        .thenReturn(randomUUID());

    // when
    underTest.handle(APPROVE_MESSAGE);

    // then
    assertThat(RequestCorrelation.id()).isEqualTo(toJavaUuid(APPROVE_MESSAGE.getCorrelationId()));
  }
}
