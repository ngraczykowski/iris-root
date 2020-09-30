package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.sens.webapp.backend.changerequest.create.CreateChangeRequestMessageHandlerFixtures.CREATE_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateChangeRequestMessageHandlerTest {

  @InjectMocks
  private CreateChangeRequestMessageHandler underTest;

  @Mock
  private ChangeRequestService changeRequestService;

  @Test
  void handleApproveMessage_changeRequestApproved() {
    // when
    underTest.handle(CREATE_MESSAGE);

    // then
    assertThat(RequestCorrelation.id()).isEqualTo(toJavaUuid(CREATE_MESSAGE.getCorrelationId()));
    verify(changeRequestService).create(
        toJavaUuid(CREATE_MESSAGE.getBulkChangeId()),
        CREATE_MESSAGE.getMakerUsername(),
        CREATE_MESSAGE.getMakerComment(),
        toOffsetDateTime(CREATE_MESSAGE.getCreatedAt()));
  }
}
