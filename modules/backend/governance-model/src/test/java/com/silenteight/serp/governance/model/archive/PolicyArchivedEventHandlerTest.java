package com.silenteight.serp.governance.model.archive;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.serp.governance.model.archive.amqp.ModelsArchivedMessageGateway;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.domain.events.PolicyArchivedEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_DTO;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyArchivedEventHandlerTest {

  @InjectMocks
  private PolicyArchivedEventHandler underTest;

  @Mock
  private ModelDetailsQuery modelDetailsQuery;

  @Mock
  private ModelsArchivedMessageGateway messageGateway;

  @Test
  void handlePolicyArchivedEventWhenThereAreModelsForPolicy() {
    // given
    var policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    var policyName = "policies/" + policyId;
    var correlationId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    when(modelDetailsQuery.getByPolicy(policyName)).thenReturn(of(MODEL_DTO));

    // when
    PolicyArchivedEvent policyArchivedEvent = PolicyArchivedEvent.builder()
        .policyId(policyId)
        .correlationId(correlationId)
        .build();
    underTest.handle(policyArchivedEvent);

    // then
    var modelsArchivedMessageCaptor = ArgumentCaptor.forClass(ModelsArchived.class);

    verify(messageGateway).send(modelsArchivedMessageCaptor.capture());

    var modelsArchivedMessage = modelsArchivedMessageCaptor.getValue();
    assertThat(modelsArchivedMessage.getModelsList()).isEqualTo(of(MODEL_RESOURCE_NAME));
  }

  @Test
  void handlePolicyArchivedEventWhenThereAreNoModelsForPolicy() {
    // given
    var policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    var policyName = "policies/" + policyId;
    var correlationId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    when(modelDetailsQuery.getByPolicy(policyName)).thenReturn(emptyList());

    // when
    PolicyArchivedEvent policyArchivedEvent = PolicyArchivedEvent.builder()
        .policyId(policyId)
        .correlationId(correlationId)
        .build();
    underTest.handle(policyArchivedEvent);

    // then
    verifyNoInteractions(messageGateway);
  }
}
