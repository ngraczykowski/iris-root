package com.silenteight.serp.governance.changerequest.approve;

import com.silenteight.serp.governance.changerequest.approve.event.ModelAcceptedEvent;
import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.APPROVED_BY;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.APPROVER_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveChangeRequestUseCaseTest {

  @Mock
  private ChangeRequestService changeRequestService;

  @Mock
  private ChangeRequestModelQuery changeRequestModelQuery;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  private ApproveChangeRequestUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ApproveChangeRequestConfiguration().approveChangeRequestUseCase(
        changeRequestService, changeRequestModelQuery, eventPublisher);
  }

  @Test
  void approveWillPublishEvent() {
    when(changeRequestModelQuery.getModel(CHANGE_REQUEST_ID)).thenReturn(MODEL_NAME);

    ApproveChangeRequestCommand approveChangeRequestCommand = ApproveChangeRequestCommand
        .builder()
        .id(CHANGE_REQUEST_ID)
        .approverComment(APPROVER_COMMENT)
        .approverUsername(APPROVED_BY)
        .build();

    useCase.activate(approveChangeRequestCommand);

    ArgumentCaptor<ModelAcceptedEvent> argumentCaptor = ArgumentCaptor
        .forClass(ModelAcceptedEvent.class);
    verify(eventPublisher).publishEvent(argumentCaptor.capture());
    ModelAcceptedEvent event = argumentCaptor.getValue();
    assertThat(event.getCorrelationId()).isEqualTo(approveChangeRequestCommand.getCorrelationId());
    assertThat(event.getModelName()).isEqualTo(MODEL_NAME);
    assertThat(event.getPromotedBy()).isEqualTo(APPROVED_BY);
  }
}
