package com.silenteight.simulator.management.grpc;

import com.silenteight.model.api.v1.ModelRequest;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcModelServiceTest {

  @InjectMocks
  private GrpcModelService underTest;

  @Mock
  private SolvingModelServiceBlockingStub modelStub;

  @Test
  void shouldGetModel() {
    // given
    when(modelStub.getSolvingModel(makeModelRequest(MODEL_NAME))).thenReturn(SOLVING_MODEL);

    // when
    SolvingModel model = underTest.getModel(MODEL_NAME);

    // then
    assertThat(model.getName()).isEqualTo(MODEL_NAME);
    assertThat(model.getPolicyName()).isEqualTo(POLICY_NAME);
    assertThat(model.getStrategyName()).isEqualTo(STRATEGY_NAME);
    assertThat(model.getCategoriesList()).isEqualTo(CATEGORIES);
  }

  private static ModelRequest makeModelRequest(String model) {
    return ModelRequest.newBuilder()
        .setModel(model)
        .build();
  }
}
