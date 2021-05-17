package com.silenteight.serp.governance.model.accept;

import com.silenteight.serp.governance.changerequest.approve.event.ModelAcceptedEvent;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.promote.PromotePolicyCommand;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelAcceptedEventListenerTest {

  private static final UUID CORRELATION_ID = randomUUID();
  @Mock
  private ModelDetailsQuery modelDetailsQuery;

  @Mock
  private PromotePolicyUseCase promotePolicyUseCase;

  private ModelAcceptedEventListener modelAcceptedEventListener;

  @BeforeEach
  void setUp() {
    modelAcceptedEventListener = new ModelAcceptConfiguration()
        .modelAcceptedEventListener(modelDetailsQuery, promotePolicyUseCase);
  }

  @Test
  void publishEventWithProperPolicyOnEvent() {
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL_DTO);

    ModelAcceptedEvent modelAcceptedEvent = ModelAcceptedEvent
        .of(CORRELATION_ID, MODEL_RESOURCE_NAME, CREATED_BY);

    modelAcceptedEventListener.handle(modelAcceptedEvent);

    ArgumentCaptor<PromotePolicyCommand> argumentCaptor = ArgumentCaptor
        .forClass(PromotePolicyCommand.class);
    verify(promotePolicyUseCase).activate(argumentCaptor.capture());
    PromotePolicyCommand event = argumentCaptor.getValue();
    assertThat(event.getCorrelationId()).isEqualTo(CORRELATION_ID);
    assertThat(event.getPolicyName()).isEqualTo(POLICY);
    assertThat(event.getPromotedBy()).isEqualTo(CREATED_BY);
  }
}
