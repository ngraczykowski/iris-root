package com.silenteight.serp.governance.model.used;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.used.amqp.ModelUsedOnProductionMessageGateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.fixture.ModelFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendModelUsedOnProductionUseCaseTest {

  @Mock
  private SolvingModelQuery solvingModelQuery;
  @Mock
  ModelUsedOnProductionMessageGateway messageGateway;
  @InjectMocks
  private SendModelUsedOnProductionUseCase underTest;

  @Test
  void activateShouldSendSolvingModel() {
    // given
    SolvingModel solvingModel = SolvingModel.getDefaultInstance();
    when(solvingModelQuery.get(MODEL_DTO)).thenReturn(solvingModel);
    ArgumentCaptor<SolvingModel> solvingModelCaptor = ArgumentCaptor.forClass(SolvingModel.class);
    //when
    underTest.activate(MODEL_DTO);
    //then
    verify(messageGateway).send(solvingModelCaptor.capture());
    assertThat(solvingModelCaptor.getValue()).isEqualTo(solvingModel);
  }
}
