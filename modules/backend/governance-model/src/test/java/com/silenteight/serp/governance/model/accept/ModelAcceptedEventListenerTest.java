package com.silenteight.serp.governance.model.accept;

import com.silenteight.serp.governance.changerequest.approve.event.ModelAcceptedEvent;
import com.silenteight.serp.governance.model.ModelProperties;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.used.ModelDeployedEvent;
import com.silenteight.serp.governance.policy.promote.PromotePolicyCommand;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
  @Mock
  private SendPromoteMessageUseCase sendPromoteMessageUseCase;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  private ModelProperties properties;

  private ModelAcceptedEventListener modelAcceptedEventListener;

  @Test
  void publishEventWithProperPolicyOnEvent() {
    createDeployableListener();
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
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void sendMessageOnPromoteEvent() {
    createDeployableListener();
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL_DTO);

    ModelAcceptedEvent modelAcceptedEvent = ModelAcceptedEvent
        .of(CORRELATION_ID, MODEL_RESOURCE_NAME, CREATED_BY);

    modelAcceptedEventListener.handle(modelAcceptedEvent);

    ArgumentCaptor<SendPromoteMessageCommand> argumentCaptor = ArgumentCaptor
        .forClass(SendPromoteMessageCommand.class);
    verify(sendPromoteMessageUseCase).activate(argumentCaptor.capture());
    SendPromoteMessageCommand command = argumentCaptor.getValue();
    assertThat(command.getCorrelationId()).isEqualTo(CORRELATION_ID);
    assertThat(command.getModelName()).isEqualTo(MODEL_RESOURCE_NAME);
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void sendEventOnLocalTransfer() {
    createLocalListener();
    when(modelDetailsQuery.get(MODEL_ID)).thenReturn(MODEL_DTO);

    ModelAcceptedEvent modelAcceptedEvent = ModelAcceptedEvent
        .of(CORRELATION_ID, MODEL_RESOURCE_NAME, CREATED_BY);

    modelAcceptedEventListener.handle(modelAcceptedEvent);

    ArgumentCaptor<ModelDeployedEvent> argumentCaptor = ArgumentCaptor
        .forClass(ModelDeployedEvent.class);
    verify(eventPublisher).publishEvent(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().getModel()).isEqualTo(MODEL_RESOURCE_NAME);
  }

  private void createDeployableListener() {
    createListener(ModelTransfer.DEPLOYABLE);
  }

  private void createLocalListener() {
    createListener(ModelTransfer.LOCAL);
  }

  private void createListener(ModelTransfer modelTransfer) {
    modelAcceptedEventListener = new ModelAcceptConfiguration().modelAcceptedEventListener(
        modelDetailsQuery,
        promotePolicyUseCase,
        sendPromoteMessageUseCase,
        new ModelProperties("", modelTransfer),
        eventPublisher);
  }
}
